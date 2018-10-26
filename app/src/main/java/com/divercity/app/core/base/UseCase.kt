package com.divercity.app.core.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by lucas on 01/03/2018.
 */
abstract class UseCase<T, Params>(private val executorThread: Scheduler, private val uiThread: Scheduler) {

    var compositeDisposable = CompositeDisposable()

    fun execute(disposableObserver: DisposableObserver<T>?, params: Params) {
       disposableObserver ?: throw IllegalArgumentException("disposableObserver cannot be null")

        val observable = this.createObservableUseCase(params).subscribeOn(executorThread).observeOn(uiThread)

        val observer = observable.subscribeWith(disposableObserver)
        compositeDisposable.add(observer)
    }

    fun dispose() {
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }

    protected abstract fun createObservableUseCase(params : Params): Observable<T>
}