package com.divercity.android.repository.registerlogin

import com.divercity.android.data.entity.emailusernamecheck.CheckUsernameEmailResponse
import com.divercity.android.model.user.User
import io.reactivex.Observable

/**
 * Created by lucas on 09/09/2018.
 */

interface RegisterLoginRepository {

    fun login(email: String, password: String): Observable<User>

    fun isEmailRegistered(email: String): Observable<CheckUsernameEmailResponse>

    fun isUsernameRegistered(username: String): Observable<CheckUsernameEmailResponse>

    fun signUp(
//        nickname: String,
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Observable<User>

    fun loginLinkedin(
        code: String,
        state: String
    ): Observable<User>

    fun loginFacebook(token: String): Observable<User>

    fun loginGoogle(token: String): Observable<User>

    fun requestResetPassword(email: String): Observable<Unit>

    fun resetPassword(
        password: String,
        token: String
    ): Observable<Unit>
}
