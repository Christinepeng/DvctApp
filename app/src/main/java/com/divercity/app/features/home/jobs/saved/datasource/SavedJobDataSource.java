package com.divercity.app.features.home.jobs.saved.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.data.entity.base.IncludedArray;
import com.divercity.app.data.entity.job.JobResponse;
import com.divercity.app.features.home.jobs.saved.usecase.FetchSavedJobsUseCase;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SavedJobDataSource extends PageKeyedDataSource<Long, JobResponse> {

    private static final String TAG = SavedJobDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchSavedJobsUseCase fetchJobsUseCase;
    private CompositeDisposable compositeDisposable;
    private String query;

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    public SavedJobDataSource(CompositeDisposable compositeDisposable,
                              FetchSavedJobsUseCase fetchJobsUseCase,
                              @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchJobsUseCase = fetchJobsUseCase;
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
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, JobResponse> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<IncludedArray<JobResponse>> disposableObserver = new DisposableObserver<IncludedArray<JobResponse>>() {
            @Override
            public void onNext(IncludedArray<JobResponse> data) {
                setRetry(() -> loadInitial(params, callback));
                if (data != null) {
                    networkState.postValue(NetworkState.LOADED);
                    if(data.getData().size() < params.requestedLoadSize)
                        callback.onResult(data.getData(), null, null);
                    else
                        callback.onResult(data.getData(), null, 2l);
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
        fetchJobsUseCase.execute(disposableObserver, FetchSavedJobsUseCase.Params.Companion.forJobs(0, params.requestedLoadSize, query));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, JobResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, JobResponse> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<IncludedArray<JobResponse>> disposableObserver = new DisposableObserver<IncludedArray<JobResponse>>() {
            @Override
            public void onNext(IncludedArray<JobResponse> data) {
                if (data != null) {
                    setRetry(null);
                    callback.onResult(data.getData(), params.key + 1);
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
        fetchJobsUseCase.execute(disposableObserver, FetchSavedJobsUseCase.Params.Companion.forJobs(params.key.intValue(), params.requestedLoadSize, query));
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
