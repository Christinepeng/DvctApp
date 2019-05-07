package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.emailusernamecheck.CheckUsernameEmailResponse
import com.divercity.android.data.entity.emailusernamecheck.emailbody.CheckEmailBody
import com.divercity.android.data.entity.emailusernamecheck.usernamebody.CheckUsernameBody
import com.divercity.android.data.entity.signup.SignUpBody
import com.divercity.android.data.entity.user.body.LoginBody
import com.divercity.android.data.entity.user.response.UserEntityResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by lucas on 09/09/2018.
 */

interface RegisterLoginService {

    @POST("auth/sign_in")
    fun login(@Body loginBody: LoginBody): Observable<Response<DataObject<UserEntityResponse>>>

    @POST("users/check_email")
    fun checkEmail(@Body loginBody: CheckEmailBody): Observable<CheckUsernameEmailResponse>

    @POST("users/check_username")
    fun checkUsername(@Body loginBody: CheckUsernameBody): Observable<CheckUsernameEmailResponse>

    @POST("auth")
    fun signUp(@Body loginBody: SignUpBody): Observable<Response<DataObject<UserEntityResponse>>>

    @GET("auth/sso/linkedin")
    fun loginLinkedin(
        @Query("code") code: String,
        @Query("state") state: String
    ): Observable<Response<DataObject<UserEntityResponse>>>

    @Multipart
    @POST("auth/sso/facebook")
    fun loginFacebook(@Part("token") token: RequestBody): Observable<Response<DataObject<UserEntityResponse>>>

    @Multipart
    @POST("passwords/forgot")
    fun requestResetPassword(@Part("email") email: RequestBody): Observable<Response<Unit>>

    @Multipart
    @POST("passwords/reset")
    fun resetPassword(
        @Part("password") password: RequestBody,
        @Part("token") token: RequestBody
    ): Observable<Response<Unit>>
}
