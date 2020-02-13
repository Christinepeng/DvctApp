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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_log_in_page.*
import kotlinx.android.synthetic.main.fragment_sign_up_page.*
import kotlinx.android.synthetic.main.fragment_sign_up_page.btn_facebook
import kotlinx.android.synthetic.main.fragment_sign_up_page.btn_facebook_sdk
import kotlinx.android.synthetic.main.fragment_sign_up_page.btn_linkedin
import kotlinx.android.synthetic.main.fragment_sign_up_page.user_email
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * Created by lucas on 24/10/2018.
 */

const val RC_SIGN_IN = 123

class LogInPageFragment : BaseFragment() {

    lateinit var viewModel: LogInPageViewModel
    private lateinit var handlerViewPager: Handler

    private val callbackManager = CallbackManager.Factory.create()

    companion object {

        fun newInstance() = LogInPageFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_log_in_page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[LogInPageViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            Timber.e("Called Launch")
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
            } catch (e: IOException) {
                e.printStackTrace()
                showToast("Error deleting notification token")
            }
        }

//        // Google log in
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        // Configure sign-in to request the user's ID, email address, and basic
//        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build()
//
//        // Google log in
//        // Build a GoogleSignInClient with the options specified by gso.
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        // Google log in
//        sign_in_button.setOnClickListener {
//            val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
//            startActivityForResult(signInIntent, RC_SIGN_IN)
//        }
//
//        // Google log in
//        val acct = GoogleSignIn.getLastSignedInAccount(activity)
//        if (acct != null) {
//            val personName = acct.displayName
//            val personGivenName = acct.givenName
//            val personFamilyName = acct.familyName
//            val personEmail = acct.email
//            val personId = acct.id
//            val personPhoto: Uri? = acct.photoUrl
//        }
    }

//    // Google log in
//    override fun onStart() {
//        super.onStart()
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
//    }

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

        btn_go_to_sign_up.setOnClickListener {
            navigator.navigateToSignUpPageFragment(requireActivity()) }

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

//        // Google log in
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode === RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task: Task<GoogleSignInAccount> =
//                GoogleSignIn.getSignedInAccountFromIntent(android.R.attr.data)
//            handleSignInResult(task)
//        }
    }

//    // Google log in
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): Unit {
//        try {
//            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
//            // Signed in successfully, show authenticated UI.
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//
//        }
//    }
}