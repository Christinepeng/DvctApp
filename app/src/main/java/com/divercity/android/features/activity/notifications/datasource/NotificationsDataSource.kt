package com.divercity.android.features.activity.notifications.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.divercity.android.core.ui.NetworkState
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import com.divercity.android.features.activity.notifications.usecase.FetchNotificationsUseCase
import com.divercity.android.features.jobs.applications.datasource.JobApplicationsDataSource
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NotificationsDataSource(
    private val fetchNotificationsUseCase: FetchNotificationsUseCase
) : PageKeyedDataSource<Long, NotificationResponse>() {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null
    private var retryDisposable: Disposable? = null

    fun retry() {
        if (retryCompletable != null) {
            retryDisposable =
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ }, { throwable -> Timber.e(throwable) })

        }
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Long>,
        callback: PageKeyedDataSource.LoadInitialCallback<Long, NotificationResponse>
    ) {
        // update network states.
        // we also provide an initial load state to the listeners so that the UI can know when the
        // very first list is loaded.
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<List<NotificationResponse>>() {

            override fun onNext(data: List<NotificationResponse>) {
                setRetry(Action { loadInitial(params, callback) })
                networkState.postValue(NetworkState.LOADED)
                val filteredList = data.filter { it.attributes?.aType != "follow" }
                if (data.size < params.requestedLoadSize)
                    callback.onResult(filteredList, null, null)
                else
                    callback.onResult(filteredList, null, 2L)
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
        fetchNotificationsUseCase.execute(
            disposableObserver,
            FetchNotificationsUseCase.Params.forFollowing(0, params.requestedLoadSize)
        )
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, NotificationResponse>
    ) {

    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Long>,
        callback: PageKeyedDataSource.LoadCallback<Long, NotificationResponse>
    ) {
        networkState.postValue(NetworkState.LOADING)

        val disposableObserver = object : DisposableObserver<List<NotificationResponse>>() {

            override fun onNext(data: List<NotificationResponse>) {
                setRetry(null)
                val filteredList = data.filter { it.attributes?.aType != "follow" }
                callback.onResult(filteredList, params.key + 1)
                networkState.postValue(NetworkState.LOADED)
            }

            override fun onError(e: Throwable) {
                setRetry(Action { loadAfter(params, callback) })
                networkState.postValue(NetworkState.error(e.message))
            }

            override fun onComplete() {

            }
        }

        fetchNotificationsUseCase.execute(
            disposableObserver,
            FetchNotificationsUseCase.Params.forFollowing(params.key.toInt(), params.requestedLoadSize)
        )
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }

    fun dispose(){
        fetchNotificationsUseCase.compositeDisposable.clear()
        if (retryDisposable != null)
            retryDisposable!!.dispose()
    }

    companion object {

        private val TAG = JobApplicationsDataSource::class.java.simpleName
    }

}

