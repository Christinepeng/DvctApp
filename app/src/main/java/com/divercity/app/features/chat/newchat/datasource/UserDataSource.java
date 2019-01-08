package com.divercity.app.features.chat.newchat.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.data.entity.user.response.UserResponse;
import com.divercity.app.features.chat.usecase.FetchUsersUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UserDataSource extends PageKeyedDataSource<Long, Object> {

    private static final String TAG = UserDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchUsersUseCase fetchUsersUseCase;
    private CompositeDisposable compositeDisposable;
    private String query;
    
    private ArrayList<Character> firstChars = new ArrayList<>();

    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;

    public UserDataSource(CompositeDisposable compositeDisposable,
                          FetchUsersUseCase fetchUsersUseCase,
                          @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchUsersUseCase = fetchUsersUseCase;
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
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, Object> callback) {
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
                    firstChars.clear();
                    networkState.postValue(NetworkState.LOADED);
                    if(data.size() < params.requestedLoadSize)
                        callback.onResult(getSegmentedList(data), null, null);
                    else
                        callback.onResult(getSegmentedList(data), null, 2L);
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
        fetchUsersUseCase.execute(disposableObserver, FetchUsersUseCase.Params.Companion.forUser(0, params.requestedLoadSize, query));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Object> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, Object> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<List<UserResponse>> disposableObserver = new DisposableObserver<List<UserResponse>>() {
            @Override
            public void onNext(List<UserResponse> data) {
                if (data != null) {
                    setRetry(null);
                    callback.onResult(getSegmentedList(data), params.key + 1);
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
        fetchUsersUseCase.execute(disposableObserver, FetchUsersUseCase.Params.Companion.forUser(params.key.intValue(), params.requestedLoadSize, query));
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

    private ArrayList<Object> getSegmentedList(List<UserResponse> data){
        ArrayList<Object> result = new ArrayList<>();

        for(UserResponse r : data){
            char firstChar = Character.toUpperCase(r.getAttributes().getName().charAt(0));
            if(!firstChars.contains(firstChar)){
                firstChars.add(firstChar);
                result.add(firstChar);
            }
            result.add(r);
        }

        return result;
    }
}
