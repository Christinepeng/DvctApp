package com.divercity.android.repository.registerlogin

import com.divercity.android.data.entity.emailusernamecheck.CheckUsernameEmailResponse
import com.divercity.android.data.entity.user.response.UserResponse

import io.reactivex.Observable

/**
 * Created by lucas on 09/09/2018.
 */

interface RegisterLoginRepository {

    fun login(email: String, password: String): Observable<UserResponse>

    fun isEmailRegistered(email: String): Observable<CheckUsernameEmailResponse>

    fun isUsernameRegistered(username: String): Observable<CheckUsernameEmailResponse>

    fun signUp(
        nickname: String,
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Observable<UserResponse>

    fun loginLinkedin(
        code: String,
        state: String
    ): Observable<UserResponse>

    fun loginFacebook(token: String): Observable<UserResponse>
}
