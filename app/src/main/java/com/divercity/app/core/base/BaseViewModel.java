package com.divercity.app.core.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by lucas on 25/09/2018.
 */

public abstract class BaseViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;

    public BaseViewModel(@NonNull Application application){
        super(application);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    protected CompositeDisposable getCompositeDisposable() {
        return compositeDisposable;
    }

}
