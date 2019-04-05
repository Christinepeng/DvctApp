package com.divercity.android.features.profile.pcurrentuser.tabconnections.datasource;

import android.util.Log;

import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.features.jobs.applications.datasource.JobApplicationsDataSource;
import com.divercity.android.features.profile.pcurrentuser.tabconnections.usecase.FetchFollowersUseCase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FollowersDataSource extends PageKeyedDataSource<Long, UserResponse> {

    private static final String TAG = JobApplicationsDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchFollowersUseCase fetchFollowersUseCase;
    private CompositeDisposable compositeDisposable;
    private String query;

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    public FollowersDataSource(CompositeDisposable compositeDisposable,
                               FetchFollowersUseCase fetchFollowersUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchFollowersUseCase = fetchFollowersUseCase;
        this.query = query;
    }

    public void retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                    }, throwable -> Log.e(TAG, throwable.getMessage())));
        }
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, UserResponse> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<List<UserResponse>> disposableObserver = new DisposableObserver<List<UserResponse>>() {
            @Override
            public void onNext(List<UserResponse> data) {
                setRetry(() -> loadInitial(params, callback));
                if (data != null) {
                    networkState.postValue(NetworkState.LOADED);
                    if(data.size() < params.requestedLoadSize)
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
        compositeDisposable.add(disposableObserver);
        fetchFollowersUseCase.execute(disposableObserver, FetchFollowersUseCase.Params.Companion.forFollowers(0, params.requestedLoadSize, null));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, UserResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, UserResponse> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<List<UserResponse>> disposableObserver = new DisposableObserver<List<UserResponse>>() {
            @Override
            public void onNext(List<UserResponse> data) {
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

        compositeDisposable.add(disposableObserver);
        fetchFollowersUseCase.execute(disposableObserver, FetchFollowersUseCase.Params.Companion.forFollowers(params.key.intValue(), params.requestedLoadSize, null));
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

}

