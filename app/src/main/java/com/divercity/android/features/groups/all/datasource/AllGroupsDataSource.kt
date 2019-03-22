package com.divercity.android.features.groups.all.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.groups.all.usecase.FetchAllGroupsUseCase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AllGroupsDataSource(
    private val fetchGroupsUseCase: FetchAllGroupsUseCase,
    private val query: String?
) : PageKeyedDataSource<Long, GroupResponse>() {

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
        callback: PageKeyedDataSource.LoadInitialCallback<Long, GroupResponse>
    ) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<DataArray<GroupResponse>>() {

            override fun onNext(data: DataArray<GroupResponse>) {
                setRetry(Action { loadInitial(params, callback) })

                networkState.postValue(NetworkState.LOADED)
                if (data.data.size < params.requestedLoadSize)
                    callback.onResult(data.data, null, null)
                else
                    callback.onResult(data.data, null, 2L)
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

        fetchGroupsUseCase.execute(
            disposableObserver,
            FetchAllGroupsUseCase.Params.forGroups(0, params.requestedLoadSize, query!!)
        )
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, GroupResponse>
    ) {

    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, GroupResponse>
    ) {
        networkState.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<DataArray<GroupResponse>>() {

            override fun onNext(data: DataArray<GroupResponse>) {
                setRetry(null)
                callback.onResult(data.data, params.key + 1)
                networkState.postValue(NetworkState.LOADED)
            }

            override fun onError(e: Throwable) {
                setRetry(Action { loadAfter(params, callback) })
                networkState.postValue(NetworkState.error(e.message))
            }

            override fun onComplete() {

            }
        }

        fetchGroupsUseCase.execute(
            disposableObserver,
            FetchAllGroupsUseCase.Params.forGroups(
                params.key.toInt(),
                params.requestedLoadSize,
                query!!
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
        fetchGroupsUseCase.compositeDisposable.clear()
        if (retryDisposable != null)
            retryDisposable!!.dispose()
    }
}
