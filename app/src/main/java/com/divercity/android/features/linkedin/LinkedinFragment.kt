package com.divercity.android.features.linkedin

import android.annotation.TargetApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_linkedin.*
import timber.log.Timber



/**
 * Created by lucas on 01/11/2018.
 */

class LinkedinFragment : BaseFragment() {

    //This is the public api key of our application
    private val API_KEY = "780fvbi7hufn1p"
    //This is the private api key of our application
    private val SECRET_KEY = "pqkN0PNzATda01f7"
    //This is any string we want to use. This will be used for avoiding CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private val STATE = "https://sandbox-api.divercity.io/api/auth/sso/linkedin"
    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
    //We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    private val REDIRECT_URI = "https://sandbox-api.divercity.io/api/auth/sso/linkedin"
    /*********************************************/

    //These are constants used for build the urls
    private val AUTHORIZATION_URL = "https://www.linkedin.com/oauth/v2/authorization"
    private val ACCESS_TOKEN_URL = "https://www.linkedin.com/oauth/v2/accessToken"
    private val SECRET_KEY_PARAM = "client_secret"
    private val RESPONSE_TYPE_PARAM = "response_type"
    private val GRANT_TYPE_PARAM = "grant_type"
    private val GRANT_TYPE = "authorization_code"
    private val RESPONSE_TYPE_VALUE = "code"
    private val CLIENT_ID_PARAM = "client_id"
    private val STATE_PARAM = "state"
    private val REDIRECT_URI_PARAM = "redirect_uri"
/*---------------------------------------*/

    private val QUESTION_MARK = "?"
    private val AMPERSAND = "&"
    private val EQUALS = "="

    lateinit var viewModel: LinkedinViewModel

    override fun layoutId(): Int = R.layout.fragment_linkedin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[LinkedinViewModel::class.java]
        clearData()
    }

    companion object {

        fun newInstance(): LinkedinFragment {
            return LinkedinFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadWeb()
        subscribeToLiveData()
    }

    private fun loadWeb(){
        showProgress()
        web_view.webViewClient = object : WebViewClient() {

            @SuppressWarnings("deprecation")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                return handleUri(Uri.parse(url))
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return handleUri(request?.url)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
//                super.onReceivedError(view, request, error)
                hideProgress()
                showDialogConnectionError()
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
//                super.onReceivedError(view, errorCode, description, failingUrl)
                hideProgress()
                showDialogConnectionError()
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//                super.onReceivedSslError(view, handler, error)
                hideProgress()
                showDialogConnectionError()
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
//                super.onReceivedHttpError(view, request, errorResponse)
                hideProgress()
                showDialogConnectionError()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
                if(url == null || !url.startsWith(REDIRECT_URI))
                    hideProgress()
            }
        }
        Timber.e("Authorize URL: ".plus(getAuthorizationUrl()))
        web_view.loadUrl(getAuthorizationUrl())
    }

    private fun showDialogConnectionError() {
        val dialog = CustomTwoBtnDialogFragment.newInstance(
                getString(R.string.ups),
                getString(R.string.error_connection),
                getString(R.string.cancel),
                getString(R.string.retry)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                loadWeb()
            }

            override fun onPositiveBtnClick() {
                activity!!.finish()
            }
        })
        dialog.isCancelable = false
        dialog.show(childFragmentManager,null)
    }

    private fun handleUri(uri: Uri?): Boolean {
        uri?.let {
            val authorizationUrl = it.toString()
            if (authorizationUrl.startsWith(REDIRECT_URI)) {
                val uri = Uri.parse(authorizationUrl)
                //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                //If not, that means the request may be a result of CSRF and must be rejected.
                val stateToken = uri.getQueryParameter(STATE_PARAM)
                if (stateToken == null || stateToken != STATE) {
                    Timber.i("State token doesn't match")
                    return true
                }

                //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                val authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE)
                if (authorizationToken == null) {
                    Timber.i("The user doesn't allow authorization.")
                    activity!!.finish()
                    return true
                }
                Timber.i("Auth token received: $authorizationToken")

                viewModel.loginLinkedin(authorizationToken, stateToken)
            } else {
                //Default behaviour
                Timber.i("Redirecting to: $authorizationUrl")
                web_view.loadUrl(authorizationUrl)
            }
        }
        return true
    }

    @Suppress("deprecation")
    private fun clearData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } else {
            val cookieSyncMngr = CookieSyncManager.createInstance(context)
            cookieSyncMngr.startSync()
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()
            cookieManager.removeSessionCookie()
            cookieSyncMngr.stopSync()
            cookieSyncMngr.sync()
        }
    }

    /**
     * Method that generates the url for get the authorization token from the Service
     * @return Url
     */
    private fun getAuthorizationUrl(): String {
        return (AUTHORIZATION_URL
                + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
                + AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND + STATE_PARAM + EQUALS + STATE
                + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI)
    }

    fun subscribeToLiveData() {
        viewModel.loginLinkedInResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }
                Status.SUCCESS -> {
                }
            }
        })

        viewModel.navigateToHome.observe(this, Observer {
            navigator.navigateToHomeActivity(activity!!)
            activity!!.finish()
        })

        viewModel.navigateToSelectUserType.observe(this, Observer {
            navigator.navigateToSelectUserTypeActivity(activity!!)
            activity!!.finish()
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()

    }
}