package com.divercity.android.repository.registerlogin

import com.divercity.android.data.entity.emailusernamecheck.CheckUsernameEmailResponse
import com.divercity.android.data.entity.emailusernamecheck.emailbody.CheckEmailBody
import com.divercity.android.data.entity.emailusernamecheck.usernamebody.CheckUsernameBody
import com.divercity.android.data.entity.signup.SignUpBody
import com.divercity.android.data.entity.user.body.LoginBody
import com.divercity.android.data.entity.user.response.UserResponse
import com.divercity.android.data.networking.services.RegisterLoginService
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by lucas on 09/09/2018.
 */

class RegisterLoginRepositoryImpl @Inject
constructor(
    private val loggedUserRepository: SessionRepository,
    private val registerLoginService: RegisterLoginService
) : RegisterLoginRepository {

    override fun login(email: String, password: String): Observable<UserResponse> {
        return registerLoginService.login(LoginBody(email, password))
            .map { response ->
                checkResponse(response)
                loggedUserRepository.saveUserHeaderData(response)
                response.body()!!.data
            }
    }

    override fun signUp(
        nickname: String,
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Observable<UserResponse> {
        return registerLoginService.signUp(
            SignUpBody(
                password,
                confirmPassword,
                nickname,
                name,
                email
            )
        ).map { response ->
            checkResponse(response)
            loggedUserRepository.saveUserHeaderData(response)
            response.body()!!.data
        }
    }

    override fun loginLinkedin(code: String, state: String): Observable<UserResponse> {
        return registerLoginService.loginLinkedin(code, state).map { response ->
            checkResponse(response)
            loggedUserRepository.saveUserHeaderData(response)
            response.body()!!.data
        }
    }

    override fun loginFacebook(token: String): Observable<UserResponse> {
        return registerLoginService.loginFacebook(
            RequestBody.create(
                MediaType.parse("text/plain"),
                token
            )
        ).map { response ->
            checkResponse(response)
            loggedUserRepository.saveUserHeaderData(response)
            response.body()!!.data
        }
    }

    override fun isEmailRegistered(email: String): Observable<CheckUsernameEmailResponse> {
        return registerLoginService.checkEmail(CheckEmailBody(email))
    }

    override fun isUsernameRegistered(username: String): Observable<CheckUsernameEmailResponse> {
        return registerLoginService.checkUsername(CheckUsernameBody(username))
    }

    private fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }
}
