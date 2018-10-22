package com.divercity.app.features.login;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.features.login.usecase.LoginUseCase;

import dagger.Binds;
import dagger.Module;

/**
 * Created by lucas on 25/09/2018.
 */

@Module
public abstract class LoginModule {

    @Binds
    abstract UseCase provideLoginUseCase(LoginUseCase useCase);

}
