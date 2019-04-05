package com.divercity.android.features.company.companyadmin.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.data.entity.company.companyadmin.CompanyAdminResponse
import com.divercity.android.features.company.companyadmin.usecase.FetchCompanyAdminsUseCase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class CompanyAdminDataSource(
    private val fetchCompanyAdminsUseCase: FetchCompanyAdminsUseCase,
    private val companyId: String
) : PageKeyedDataSource<Long, CompanyAdminResponse>() {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()
    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null
    private var disposableRetry: Disposable? = null

    fun retry() {
        if (retryCompletable != null) {
            disposableRetry = retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { throwable -> Timber.e(throwable) })
        }
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, CompanyAdminResponse>
    ) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<List<CompanyAdminResponse>>() {

            override fun onNext(data: List<CompanyAdminResponse>) {
                setRetry(Action { loadInitial(params, callback) })
                networkState.postValue(NetworkState.LOADED)
                if (data.size < params.requestedLoadSize) {
                    callback.onResult(data, null, null)
                } else {
                    callback.onResult(data, null, 2L)
                }
                initialLoad.postValue(NetworkState.LOADED)
            }

            override fun onError(e: Throwable) {
                setRetry(Action { loadInitial(params, callback) })
                val error = NetworkState.error("Error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }

            override fun onComplete() {

            }
        }
        fetchCompanyAdminsUseCase.execute(
            disposableObserver,
            FetchCompanyAdminsUseCase.Params.forAdmin(companyId,0, params.requestedLoadSize)
        )
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, CompanyAdminResponse>
    ) {

    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, CompanyAdminResponse>
    ) {
        networkState.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<List<CompanyAdminResponse>>() {

            override fun onNext(data: List<CompanyAdminResponse>) {
                setRetry(null)
                callback.onResult(data, params.key + 1)
                networkState.postValue(NetworkState.LOADED)
            }

            override fun onError(e: Throwable) {
                setRetry(Action { loadAfter(params, callback) })
                networkState.postValue(NetworkState.error(e.message))
            }

            override fun onComplete() {

            }
        }

        fetchCompanyAdminsUseCase.execute(
            disposableObserver,
            FetchCompanyAdminsUseCase.Params.forAdmin(
                companyId,
                params.key.toInt(),
                params.requestedLoadSize
            )
        )
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    fun dispose() {
        fetchCompanyAdminsUseCase.compositeDisposable.clear()
        if (disposableRetry != null)
            disposableRetry!!.dispose()
    }
}
