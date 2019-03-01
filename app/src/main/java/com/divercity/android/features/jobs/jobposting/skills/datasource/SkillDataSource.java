package com.divercity.android.features.jobs.jobposting.skills.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.data.entity.base.DataArray;
import com.divercity.android.data.entity.skills.SkillResponse;
import com.divercity.android.features.jobs.jobposting.skills.usecase.FetchSkillsUseCase;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SkillDataSource extends PageKeyedDataSource<Long, SkillResponse> {

    private static final String TAG = SkillDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchSkillsUseCase fetchSkillsUseCase;
    private CompositeDisposable compositeDisposable;
    private String query;

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    public SkillDataSource(CompositeDisposable compositeDisposable,
                           FetchSkillsUseCase fetchSkillsUseCase,
                           @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchSkillsUseCase = fetchSkillsUseCase;
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
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, SkillResponse> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<DataArray<SkillResponse>> disposableObserver = new DisposableObserver<DataArray<SkillResponse>>() {
            @Override
            public void onNext(DataArray<SkillResponse> data) {
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
        fetchSkillsUseCase.execute(disposableObserver, FetchSkillsUseCase.Params.Companion.forSkills(0, params.requestedLoadSize, query));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, SkillResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, SkillResponse> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<DataArray<SkillResponse>> disposableObserver = new DisposableObserver<DataArray<SkillResponse>>() {
            @Override
            public void onNext(DataArray<SkillResponse> data) {
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
        fetchSkillsUseCase.execute(disposableObserver, FetchSkillsUseCase.Params.Companion.forSkills(params.key.intValue(), params.requestedLoadSize, query));
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
