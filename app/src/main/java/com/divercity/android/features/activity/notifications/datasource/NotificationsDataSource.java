package com.divercity.android.features.activity.notifications.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;
import android.util.Log;

import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.data.entity.activity.notification.NotificationResponse;
import com.divercity.android.features.activity.notifications.usecase.FetchNotificationsUseCase;
import com.divercity.android.features.jobs.applications.datasource.JobApplicationsDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NotificationsDataSource extends PageKeyedDataSource<Long, NotificationResponse> {

    private static final String TAG = JobApplicationsDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchNotificationsUseCase fetchNotificationsUseCase;
    private CompositeDisposable compositeDisposable;

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    public NotificationsDataSource(CompositeDisposable compositeDisposable,
                                   FetchNotificationsUseCase fetchNotificationsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchNotificationsUseCase = fetchNotificationsUseCase;
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
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, NotificationResponse> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<List<NotificationResponse>> disposableObserver = new DisposableObserver<List<NotificationResponse>>() {
            @Override
            public void onNext(List<NotificationResponse> data) {
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
        fetchNotificationsUseCase.execute(disposableObserver, FetchNotificationsUseCase.Params.Companion.forFollowing(0, params.requestedLoadSize));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, NotificationResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, NotificationResponse> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<List<NotificationResponse>> disposableObserver = new DisposableObserver<List<NotificationResponse>>() {
            @Override
            public void onNext(List<NotificationResponse> data) {
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
        fetchNotificationsUseCase.execute(disposableObserver, FetchNotificationsUseCase.Params.Companion.forFollowing(params.key.intValue(), params.requestedLoadSize));
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
