package com.divercity.android.features.home.home.feed.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.data.entity.home.HomeItem;
import com.divercity.android.data.entity.questions.QuestionResponse;
import com.divercity.android.features.home.home.usecase.FetchFeedRecommendedJobsGroupsUseCase;
import com.divercity.android.features.home.home.usecase.GetQuestionsUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class GroupsInterestsDataSource extends PageKeyedDataSource<Long, HomeItem> {

    private static final String TAG = GroupsInterestsDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private GetQuestionsUseCase getQuestionsUseCase;
    private FetchFeedRecommendedJobsGroupsUseCase fetchFeedRecommendedJobsGroupsUseCase;
    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;
    private Disposable disposableRetry;

    public GroupsInterestsDataSource(GetQuestionsUseCase getQuestionsUseCase,
                                     FetchFeedRecommendedJobsGroupsUseCase fetchFeedRecommendedJobsGroupsUseCase) {
        this.getQuestionsUseCase = getQuestionsUseCase;
        this.fetchFeedRecommendedJobsGroupsUseCase = fetchFeedRecommendedJobsGroupsUseCase;
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
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, HomeItem> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<List<HomeItem>> disposableObserver = new DisposableObserver<List<HomeItem>>() {
            @Override
            public void onNext(List<HomeItem> data) {
                setRetry(() -> loadInitial(params, callback));
                if (data != null) {
                    networkState.postValue(NetworkState.LOADED);
                    if (data.size() < params.requestedLoadSize)
                        callback.onResult(data, null, null);
                    else
                        callback.onResult(data, null, 2L);
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
        fetchFeedRecommendedJobsGroupsUseCase.execute(
                disposableObserver,
                FetchFeedRecommendedJobsGroupsUseCase.Params.Companion.forJobs(0, params.requestedLoadSize, null));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, HomeItem> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, HomeItem> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<List<QuestionResponse>> disposableObserver = new DisposableObserver<List<QuestionResponse>>() {
            @Override
            public void onNext(List<QuestionResponse> data) {
                if (data != null) {
                    setRetry(null);
                    ArrayList<HomeItem> res = new ArrayList<>(data);
                    callback.onResult(res, params.key + 1);
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

        getQuestionsUseCase.execute(disposableObserver, GetQuestionsUseCase.Params.forInterests(params.key.intValue(), params.requestedLoadSize));
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
        fetchFeedRecommendedJobsGroupsUseCase.dispose();
        getQuestionsUseCase.dispose();
        if(disposableRetry != null)
            disposableRetry.dispose();
    }
}
