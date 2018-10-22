package com.divercity.app.features.onboarding;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.divercity.app.R;
import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.utils.SingleLiveEvent;
import com.divercity.app.core.utils.Util;
import com.divercity.app.data.Resource;
import com.divercity.app.features.onboarding.usecase.ChecIskEmailRegisteredUseCase;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by lucas on 26/09/2018.
 */

public class OnboardingViewModel extends BaseViewModel {

    private ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase;
    public ObservableField<String> userEmail = new ObservableField<>();

    private SingleLiveEvent<Integer> navigateTo = new SingleLiveEvent<>();
    private SingleLiveEvent<Resource<Boolean>> isEmailRegistered = new SingleLiveEvent<>();

    @Inject
    public OnboardingViewModel(@NonNull Application application, ChecIskEmailRegisteredUseCase checIskEmailRegisteredUseCase) {
        super(application);
        this.checIskEmailRegisteredUseCase = checIskEmailRegisteredUseCase;
    }

    public void checkIfEmailRegistered(String email) {
        if (Util.isValidEmail(email)) {
            isEmailRegistered.setValue(Resource.loading(null));
            DisposableObserver<Boolean> disposable = new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean r) {
                    isEmailRegistered.setValue(Resource.success(r));
                }

                @Override
                public void onError(Throwable e) {
                    isEmailRegistered.setValue(Resource.error(e.getMessage(),null));
                }

                @Override
                public void onComplete() {

                }
            };
            getCompositeDisposable().add(disposable);
            checIskEmailRegisteredUseCase.execute(disposable, ChecIskEmailRegisteredUseCase.Params.forCheckEmail(email));
        } else
            isEmailRegistered.setValue(Resource.error(getApplication().getResources().getString(R.string.insert_valid_email), null));
    }

    public MutableLiveData<Resource<Boolean>> getIsEmailRegistered() {
        return isEmailRegistered;
    }

    public SingleLiveEvent<Integer> getNavigateTo() {
        return navigateTo;
    }

}
