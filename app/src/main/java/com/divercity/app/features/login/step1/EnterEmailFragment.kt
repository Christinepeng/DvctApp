package com.divercity.app.features.login.step1

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewPager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.divercity.app.AppConstants
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.ViewPagerDotsPanel
import com.divercity.app.data.Status
import com.divercity.app.features.login.step1.usecase.ConnectFacebookApiHelper
import com.facebook.internal.CallbackManagerImpl
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_enter_email_linear.*
import javax.inject.Inject


/**
 * Created by lucas on 24/10/2018.
 */

class EnterEmailFragment : BaseFragment() {

    @Inject
    lateinit var viewPagerEnterEmailAdapter: ViewPagerEnterEmailAdapter

    lateinit var connectFacebookApiHelper: ConnectFacebookApiHelper

    lateinit var viewModel: EnterEmailViewModel
    private lateinit var handlerViewPager: Handler

    companion object {

        fun newInstance() = EnterEmailFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_enter_email_linear

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[EnterEmailViewModel::class.java]
        connectFacebookApiHelper = ConnectFacebookApiHelper(this)
        connectFacebookApiHelper.setListener(facebookListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlerViewPager = Handler()
        setupViewPager()
        subscribeToLiveData()
        setupEvents()
    }

    fun subscribeToLiveData() {
        viewModel.isEmailRegistered.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
//                    showSnackbar(response.message)
                    showToast(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })

        viewModel.loginFacebookResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    if (response.data?.attributes?.accountType == null)
                        navigator.navigateToSelectUserTypeActivity(activity!!)
                    else
                        navigator.navigateToHomeActivity(activity!!)
                    activity!!.finish()
                }
            }
        })

        viewModel.navigateToLogin.observe(this, Observer {
            navigator.navigateToLoginActivity(activity!!, user_email.text.toString().trim())
        })

        viewModel.navigateToSignUp.observe(this, Observer {
            navigator.navigateToSignUpActivity(activity!!, user_email.text.toString().trim())
        })
    }

    fun showSnackbar(message: String?) {
        activity?.run {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    message ?: "Error",
                    Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun setupEvents() {
        btnFacebook.setOnClickListener {
            connectFacebookApiHelper.connectToFacebook()
        }

        btn_linkedin.setOnClickListener {
            navigator.navigateToLinkedinActivity(activity!!)
        }

        btn_send.setOnClickListener {
            viewModel.checkIfEmailRegistered(user_email.text.toString())
        }

        user_email.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.checkIfEmailRegistered(user_email.text.toString())
                handled = true
            }
            handled
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun setupViewPager() {
        viewPager.adapter = viewPagerEnterEmailAdapter
        val viewPagerDotsPanel = ViewPagerDotsPanel(
                context,
                viewPagerEnterEmailAdapter.count,
                sliderDots
        )

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                viewPagerDotsPanel.onPageSelected(position)
                handlerViewPager.removeCallbacksAndMessages(null)
                handlerViewPager.postDelayed(runnable, AppConstants.CAROUSEL_PAGES_DELAY)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        handlerViewPager.postDelayed(runnable, AppConstants.CAROUSEL_PAGES_DELAY)
    }

    private val runnable = Runnable {
        viewPager.currentItem =
                if (viewPager.currentItem == viewPagerEnterEmailAdapter.count - 1)
                    0
                else
                    viewPager.currentItem + 1
    }

    override fun onDestroyView() {
        handlerViewPager.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode())
            connectFacebookApiHelper.facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private var facebookListener = ConnectFacebookApiHelper.OnFacebookDataListener {
        viewModel.loginFacebook(it.accessToken)
    }
}