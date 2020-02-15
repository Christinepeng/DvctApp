package com.divercity.android.features.signup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
//import com.divercity.android.features.login.step1.SignUpPageViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_log_in_page.*
import kotlinx.android.synthetic.main.fragment_log_in_page.btn_facebook
import kotlinx.android.synthetic.main.fragment_log_in_page.btn_facebook_sdk
import kotlinx.android.synthetic.main.fragment_log_in_page.btn_linkedin
import kotlinx.android.synthetic.main.fragment_sign_up_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * Created by lucas on 24/10/2018.
 */

const val RC_SIGN_IN = 123

class SignUpPageFragment : BaseFragment() {

    lateinit var viewModel: SignUpPageViewModel
    private lateinit var handlerViewPager: Handler

    private val callbackManager = CallbackManager.Factory.create()

    companion object {
        private const val PARAM_EMAIL = "paramEmail"
        fun newInstance() = SignUpPageFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_sign_up_page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SignUpPageViewModel::class.java]

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
        hideKeyboard()
//        setupViewPager()
//        setupToolbar()
        setupEvents()
        subscribeToLiveData()
    }

    fun subscribeToLiveData() {
        viewModel.checkEmailRegisteredResponse.observe(this, Observer { response ->
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

        viewModel.loginEmailResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showSnackbar(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    if (response.data?.accountType == null)
                        navigator.navigateToSelectUserTypeActivity(requireActivity())
                    else
                        navigator.navigateToHomeActivity(requireActivity())
                }
            }
        })

        viewModel.requestResetPasswordResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showSnackbar(response.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showConfirmEmailSentDialog()
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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("451214312845-f7of8g813n0o5m44o52g7p5b17sup578.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

//        mGoogleSignInClient.signOut()

        btn_google_login_sign_up_page.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(requireActivity())
            if (account != null) {
                mGoogleSignInClient.signOut().addOnCompleteListener { task ->
                    if (task.isComplete) {
                        startGoogleSignInActivity(mGoogleSignInClient)
                    }
                }
            } else { // User is not yet signed in: Start the Google sign-in flow.
                startGoogleSignInActivity(mGoogleSignInClient)
            }
        }

        btn_linkedin.setOnClickListener {
            navigator.navigateToLinkedinActivity(requireActivity())
        }

        btn_go_to_log_in.setOnClickListener {
            navigator.navigateToLogInPageFragment(requireActivity())
        }

        sign_up_user_email.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.checkIfEmailRegistered(getEdTxtEmail())
                handled = true
            }
            handled
        }

//        btn_forget_password.setOnClickListener {
//            viewModel.requestResetPassword(user_email.text.toString())
//        }

        btn_sign_up.setOnClickListener {
            viewModel.loginEmail(sign_up_user_email.text.toString(), sign_up_user_password.text.toString())
        }

        sign_up_user_password.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.loginEmail(sign_up_user_email.text.toString(), sign_up_user_password.text.toString())
                handled = true
            }
            handled
        }
    }

    fun getEdTxtEmail(): String {
        return user_email.text.toString().trim()
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

    private fun showConfirmEmailSentDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.confirm_email),
            getString(R.string.email_been_sent),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener { }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handlerViewPager.removeCallbacksAndMessages(null)
    }

    private fun startGoogleSignInActivity(mGoogleSignInClient: GoogleSignInClient) {
        val signInIntent: Intent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.d("TAG", "CLICK2")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.getIdToken()
                Log.d("TAG", "Google Token: " + idToken)
//                viewModel.loginGoogle(idToken)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("lol", "signInResult:failed code=" + e.getStatusCode());
                // ...
            }
        }
    }
}