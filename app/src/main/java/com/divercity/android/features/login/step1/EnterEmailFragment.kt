package com.divercity.android.features.login.step1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_enter_email.*
import kotlinx.android.synthetic.main.fragment_enter_email_linear.*
import kotlinx.android.synthetic.main.fragment_enter_email_linear.btn_linkedin
import kotlinx.android.synthetic.main.fragment_enter_email_linear.user_email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * Created by lucas on 24/10/2018.
 */

class EnterEmailFragment : BaseFragment() {

    lateinit var viewModel: EnterEmailViewModel
    private lateinit var handlerViewPager: Handler

    private val callbackManager = CallbackManager.Factory.create()

    companion object {

        fun newInstance() = EnterEmailFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_enter_email_linear

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[EnterEmailViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            Timber.e("Called Launch")
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("Error deleting notification token")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlerViewPager = Handler()
//        setupViewPager()
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
                    if (response.data?.accountType == null)
                        navigator.navigateToSelectUserTypeActivity(requireActivity())
                    else
                        navigator.navigateToHomeActivity(requireActivity())
                    requireActivity().finish()
                }
            }
        })

        viewModel.navigateToLogin.observe(this, Observer {
            navigator.navigateToLoginActivity(requireActivity(), getEdTxtEmail())
        })

        viewModel.navigateToSignUp.observe(this, Observer {
            navigator.navigateToSignUpActivity(requireActivity(), getEdTxtEmail())
        })
    }

    private fun setupEvents() {
        btn_facebook.setOnClickListener {
            btn_facebook_sdk.performClick()
        }

        // Callback registration
        btn_facebook_sdk.fragment = this
        btn_facebook_sdk.setPermissions(listOf("email"))
        btn_facebook_sdk.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                viewModel.loginFacebook(loginResult.accessToken.token)
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(exception: FacebookException) {
                showToast(exception.message)
            }
        })


        btn_linkedin.setOnClickListener {
            navigator.navigateToLinkedinActivity(requireActivity())
        }

//        btn_send.setOnClickListener {
//            viewModel.checkIfEmailRegistered(getEdTxtEmail())
//        }

        user_email.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.checkIfEmailRegistered(getEdTxtEmail())
                handled = true
            }
            handled
        }
    }

    fun getEdTxtEmail(): String {
        return user_email.text.toString().trim()
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handlerViewPager.removeCallbacksAndMessages(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}