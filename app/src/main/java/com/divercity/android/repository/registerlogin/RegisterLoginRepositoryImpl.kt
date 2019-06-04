package com.divercity.android.repository.registerlogin

import com.divercity.android.core.base.BaseRepository
import com.divercity.android.data.entity.emailusernamecheck.CheckUsernameEmailResponse
import com.divercity.android.data.entity.emailusernamecheck.emailbody.CheckEmailBody
import com.divercity.android.data.entity.emailusernamecheck.usernamebody.CheckUsernameBody
import com.divercity.android.data.entity.signup.SignUpBody
import com.divercity.android.data.entity.user.body.LoginBody
import com.divercity.android.data.networking.services.RegisterLoginService
import com.divercity.android.model.user.User
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

/**
 * Created by lucas on 09/09/2018.
 */

class RegisterLoginRepositoryImpl @Inject
constructor(
    private val sessionRepository: SessionRepository,
    private val registerLoginService: RegisterLoginService
) : BaseRepository(), RegisterLoginRepository {

    override fun login(email: String, password: String): Observable<User> {
        return registerLoginService.login(LoginBody(email, password))
            .map { response ->
                checkResponse(response)
                sessionRepository.saveUserHeaderData(response)
                response.body()!!.data.toUser()
            }
    }

    override fun signUp(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Observable<User> {
        return registerLoginService.signUp(
            SignUpBody(
                password,
                confirmPassword,
                name,
                email
            )
        ).map { response ->
            checkResponse(response)
            sessionRepository.saveUserHeaderData(response)
            response.body()!!.data.toUser()
        }
    }

    override fun loginLinkedin(code: String, state: String): Observable<User> {
        return registerLoginService.loginLinkedin(code, state).map { response ->
            checkResponse(response)
            sessionRepository.saveUserHeaderData(response)
            response.body()!!.data.toUser()
        }
    }

    override fun loginFacebook(token: String): Observable<User> {
        return registerLoginService.loginFacebook(
            RequestBody.create(
                MediaType.parse("text/plain"),
                token
            )
        ).map { response ->
            checkResponse(response)
            sessionRepository.saveUserHeaderData(response)
            response.body()!!.data.toUser()
        }
    }

    override fun isEmailRegistered(email: String): Observable<CheckUsernameEmailResponse> {
        return registerLoginService.checkEmail(CheckEmailBody(email))
    }

    override fun isUsernameRegistered(username: String): Observable<CheckUsernameEmailResponse> {
        return registerLoginService.checkUsername(CheckUsernameBody(username))
    }

    override fun requestResetPassword(email: String): Observable<Unit> {
        val partEmail = RequestBody.create(MediaType.parse("text/plain"), email)
        return registerLoginService.requestResetPassword(partEmail).map {
            checkResponse(it)
        }
    }

    override fun resetPassword(password: String, token: String): Observable<Unit> {
        val partPassword = RequestBody.create(MediaType.parse("text/plain"), password)
        val partToken = RequestBody.create(MediaType.parse("text/plain"), token)
        return registerLoginService.resetPassword(partPassword, partToken).map {
            checkResponse(it)
        }
    }
}
