package com.divercity.android.core.base.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.ui.NetworkState
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by lucas on 11/04/2019.
 */

class BaseDataSource<T>(
    private val useCase: UseCase<List<T>, Params>,
    private val search: String?
) : PageKeyedDataSource<Long, T>() {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null
    private var retryDisposable: Disposable? = null

    fun retry() {
        if (retryCompletable != null) {
            retryDisposable = retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { throwable -> Timber.e(throwable) })
        }
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, T>
    ) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<List<T>>() {

            override fun onNext(data: List<T>) {
                setRetry(Action { loadInitial(params, callback) })

                networkState.postValue(NetworkState.LOADED)
                if (data.size < params.requestedLoadSize)
                    callback.onResult(data, null, null)
                else
                    callback.onResult(data, null, 2L)
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

        useCase.execute(
            disposableObserver,
            Params(0, params.requestedLoadSize, search)
        )
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, T>
    ) {

    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, T>
    ) {
        networkState.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<List<T>>() {

            override fun onNext(data: List<T>) {
                setRetry(null)
                if (data.size < params.requestedLoadSize)
                    callback.onResult(data, null)
                else
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

        useCase.execute(
            disposableObserver,
            Params(params.key.toInt(), params.requestedLoadSize, search)
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
        useCase.compositeDisposable.clear()
        if (retryDisposable != null)
            retryDisposable!!.dispose()
    }
}