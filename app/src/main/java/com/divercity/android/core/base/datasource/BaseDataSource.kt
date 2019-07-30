package com.divercity.android.core.base.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.google.gson.JsonElement
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
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
        retryCompletable?.let {
            retryDisposable = it
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { throwable -> Timber.e(throwable) })
        }
    }

    private fun onInitialError(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, T>,
        message: String

    ) {
        setRetry(Action { loadInitial(params, callback) })
        val error = NetworkState.error(message)
        networkState.postValue(error)
        initialLoad.postValue(error)
    }

    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, T>
    ) {
        // Update network states.
        // We also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        val callbackUseCase = object : DisposableObserverWrapper<List<T>>() {

            override fun onFail(error: String) {
                onInitialError(params, callback, error)
            }

            override fun onHttpException(error: JsonElement) {
                onInitialError(params, callback, error.toString())
            }

            override fun onSuccess(o: List<T>) {
                setRetry(null)
                networkState.postValue(NetworkState.LOADED)
                if (o.size < params.requestedLoadSize)
                    callback.onResult(o, null, null)
                else
                    callback.onResult(o, null, 2L)
                initialLoad.postValue(NetworkState.LOADED)
            }
        }

        useCase.execute(
            callbackUseCase,
            Params(0, params.requestedLoadSize, search)
        )
    }

    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, T>
    ) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, T>
    ) {
        networkState.postValue(NetworkState.LOADING)

        val callbackUseCase = object : DisposableObserverWrapper<List<T>>() {

            override fun onFail(error: String) {
                setRetry(Action { loadAfter(params, callback) })
                networkState.postValue(NetworkState.error(error))
            }

            override fun onHttpException(error: JsonElement) {
                setRetry(Action { loadAfter(params, callback) })
                networkState.postValue(NetworkState.error(error.toString()))
            }

            override fun onSuccess(o: List<T>) {
                setRetry(null)
                if (o.size < params.requestedLoadSize)
                    callback.onResult(o, null)
                else
                    callback.onResult(o, params.key + 1)
                networkState.postValue(NetworkState.LOADED)
            }
        }

        useCase.execute(
            callbackUseCase,
            Params(params.key.toInt(), params.requestedLoadSize, search)
        )
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    fun dispose() {
        useCase.compositeDisposable.clear()
        retryDisposable?.dispose()
    }
}