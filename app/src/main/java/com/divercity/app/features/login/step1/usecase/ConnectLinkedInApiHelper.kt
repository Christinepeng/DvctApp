package com.divercity.app.features.login.step1.usecase

import android.app.Application
import javax.inject.Inject

/**
 * Created by lucas on 30/10/2018.
 */

class ConnectLinkedInApiHelper @Inject
constructor(var application: Application) {

//    interface Listener {
//
//        fun onAuthSucces(token: AccessToken?)
//
//        fun onAuthError(msg: String)
//    }
//
//    fun getLinkedInToken(activity: FragmentActivity?, listener: Listener) {
//        LISessionManager.getInstance(application).init(
//            activity,
//            Scope.build(Scope.R_BASICPROFILE),
//            object : AuthListener {
//                override fun onAuthSuccess() {
//                    val sessionManager = LISessionManager.getInstance(activity!!.applicationContext)
//                    val session = sessionManager.session
//                    if (session.isValid)
//                        listener.onAuthSucces(session.accessToken)
//                    else
//                        listener.onAuthError("LinkedIn token error")
//                }
//
//                override fun onAuthError(error: LIAuthError?) {
//                    listener.onAuthError(error.toString())
//                }
//            },
//            true
//        )
//    }
//
//    fun getLinkedInPackageHash(activity: AppCompatActivity?) {
//        try {
//
//            val info = activity!!.packageManager.getPackageInfo(
//                BuildConfig.APPLICATION_ID, //give your package name here
//                PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//
//                Timber.e("Hash  : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP))//Key hash is printing in Log
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//            Timber.d(e.message)
//        } catch (e: NoSuchAlgorithmException) {
//            Timber.d(e.message)
//        }
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        LISessionManager.getInstance(application).onActivityResult(null, requestCode, resultCode, data)
//    }


}