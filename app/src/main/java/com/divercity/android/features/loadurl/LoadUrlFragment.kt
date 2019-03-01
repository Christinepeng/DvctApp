package com.divercity.android.features.loadurl

import android.annotation.TargetApi
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_linkedin.*


/**
 * Created by lucas on 01/11/2018.
 */

class LoadUrlFragment : BaseFragment() {

    var url: String? = null

    override fun layoutId(): Int = R.layout.fragment_linkedin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clearData()
    }

    companion object {
        private const val PARAM_URL = "paramUrl"

        fun newInstance(jobId: String): LoadUrlFragment {
            val fragment = LoadUrlFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_URL, jobId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = arguments?.getString(PARAM_URL)
        web_view.settings.setSupportZoom(true)
        web_view.settings.javaScriptEnabled = true
        web_view.loadUrl(url)
//        loadWeb()
    }

    private fun loadWeb() {

        web_view.webViewClient = object : WebViewClient() {

            @SuppressWarnings("deprecation")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }

            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return super.shouldOverrideUrlLoading(view,request)
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
                if(url == null)
                    hideProgress()
            }
        }
//        Timber.e("Authorize URL: ".plus(url))
        web_view.settings.javaScriptEnabled
        web_view.loadUrl(url)
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
        dialog.show(childFragmentManager, null)
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

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}