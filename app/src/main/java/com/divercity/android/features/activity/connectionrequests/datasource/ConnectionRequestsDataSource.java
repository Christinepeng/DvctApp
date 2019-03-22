package com.divercity.android.features.activity.connectionrequests.datasource;

import android.util.Log;

import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.data.entity.group.ConnectionItem;
import com.divercity.android.features.activity.connectionrequests.usecase.FetchConnectionRequestsUseCase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ConnectionRequestsDataSource extends PageKeyedDataSource<Long, ConnectionItem> {

    private static final String TAG = ConnectionRequestsDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchConnectionRequestsUseCase fetchConnectionRequestsUseCase;
    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;
    private Disposable disposableRetry;

    public ConnectionRequestsDataSource(FetchConnectionRequestsUseCase fetchConnectionRequestsUseCase) {
        this.fetchConnectionRequestsUseCase = fetchConnectionRequestsUseCase;
    }

    public void retry() {
        if (retryCompletable != null) {
            disposableRetry = retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> Log.e(TAG, throwable.getMessage()));
        }
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, ConnectionItem> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<List<ConnectionItem>> disposableObserver = new DisposableObserver<List<ConnectionItem>>() {
            @Override
            public void onNext(List<ConnectionItem> data) {
                setRetry(() -> loadInitial(params, callback));
                if (data != null) {
                    networkState.postValue(NetworkState.LOADED);
                    if (data.size() < params.requestedLoadSize)
                        callback.onResult(data, null, null);
                    else
                        callback.onResult(data, null, 2l);
                    initialLoading.postValue(NetworkState.LOADED);
                } else {
                    NetworkState error = NetworkState.error("Error");
                    networkState.postValue(error);
                    initialLoading.postValue(error);
                }
            }

            @Override
            public void onError(Throwable e) {
                setRetry(() -> loadInitial(params, callback));
                NetworkState error = NetworkState.error("Error");
                networkState.postValue(error);
                initialLoading.postValue(error);
            }

            @Override
            public void onComplete() {

            }
        };
        fetchConnectionRequestsUseCase.execute(disposableObserver, FetchConnectionRequestsUseCase.Params.Companion.forConnRequest(0, params.requestedLoadSize));
    }

        @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, ConnectionItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, ConnectionItem> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<List<ConnectionItem>> disposableObserver = new DisposableObserver<List<ConnectionItem>>() {
            @Override
            public void onNext(List<ConnectionItem> data) {
                if (data != null) {
                    setRetry(null);
                    callback.onResult(data, params.key + 1);
                    networkState.postValue(NetworkState.LOADED);
                } else {
                    setRetry(() -> loadAfter(params, callback));                // publish the error
                    networkState.postValue(NetworkState.error("Error"));
                }
            }

            @Override
            public void onError(Throwable e) {
                setRetry(() -> loadAfter(params, callback));              // publish the error
                networkState.postValue(NetworkState.error(e.getMessage()));
            }

            @Override
            public void onComplete() {

            }
        };

        fetchConnectionRequestsUseCase.execute(disposableObserver, FetchConnectionRequestsUseCase.Params.Companion.forConnRequest(params.key.intValue(), params.requestedLoadSize));
    }

    @NonNull
    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @NonNull
    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoading;
    }

    private void setRetry(final Action action) {
        if (action == null) {
            this.retryCompletable = null;
        } else {
            this.retryCompletable = Completable.fromAction(action);
        }
    }

    public void dispose(){
        fetchConnectionRequestsUseCase.getCompositeDisposable().clear();
        if(disposableRetry != null)
            disposableRetry.dispose();
    }
}
