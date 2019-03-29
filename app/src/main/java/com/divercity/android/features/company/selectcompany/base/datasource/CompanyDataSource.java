package com.divercity.android.features.company.selectcompany.base.datasource;

import android.util.Log;

import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.features.company.selectcompany.base.usecase.FetchCompaniesUseCase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CompanyDataSource extends PageKeyedDataSource<Long, CompanyResponse> {

    private static final String TAG = CompanyDataSource.class.getSimpleName();

    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private MutableLiveData<NetworkState> initialLoading = new MutableLiveData<>();

    private FetchCompaniesUseCase fetchCompaniesUseCase;
    private String query;
    /**
     * Keep Completable reference for the retry event
     */
    private Completable retryCompletable;
    private Disposable disposableRetry;

    public CompanyDataSource(FetchCompaniesUseCase fetchCompaniesUseCase,
                             @Nullable String query) {
        this.fetchCompaniesUseCase = fetchCompaniesUseCase;
        this.query = query;
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
    public void loadInitial(@NonNull final LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, CompanyResponse> callback) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING);
        initialLoading.postValue(NetworkState.LOADING);

        DisposableObserver<List<CompanyResponse>> disposableObserver = new DisposableObserver<List<CompanyResponse>>() {
            @Override
            public void onNext(List<CompanyResponse> data) {
                setRetry(() -> loadInitial(params, callback));
                if (data != null) {
                    networkState.postValue(NetworkState.LOADED);
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
        fetchCompaniesUseCase.execute(disposableObserver, FetchCompaniesUseCase.Params.Companion.forCompanies(0, params.requestedLoadSize, query));
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, CompanyResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, CompanyResponse> callback) {
        networkState.postValue(NetworkState.LOADING);

        DisposableObserver<List<CompanyResponse>> disposableObserver = new DisposableObserver<List<CompanyResponse>>() {
            @Override
            public void onNext(List<CompanyResponse> data) {
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
        fetchCompaniesUseCase.execute(disposableObserver, FetchCompaniesUseCase.Params.Companion.forCompanies(params.key.intValue(), params.requestedLoadSize, query));
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
        fetchCompaniesUseCase.getCompositeDisposable().clear();
        if(disposableRetry != null)
            disposableRetry.dispose();
    }
}
