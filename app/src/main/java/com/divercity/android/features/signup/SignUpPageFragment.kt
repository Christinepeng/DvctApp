package com.divercity.android.features.signup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.ImageUtils
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import com.divercity.android.features.login.step1.DIVERCITY_USERNAME_PASSWORD_PREF
import com.divercity.android.features.login.step1.PASSWORD
import com.divercity.android.features.login.step1.REMEMBER_USERNAME_PASSWORD
import com.divercity.android.features.login.step1.USERNAME
//import com.divercity.android.features.login.step1.SignUpPageViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.fragment_log_in_page.*
import kotlinx.android.synthetic.main.fragment_log_in_page.btn_facebook
import kotlinx.android.synthetic.main.fragment_log_in_page.btn_facebook_sdk
import kotlinx.android.synthetic.main.fragment_log_in_page.btn_linkedin
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up_page.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.IOException

/**
 * Created by lucas on 24/10/2018.
 */

const val RC_SIGN_IN = 123

class SignUpPageFragment : BaseFragment() {

    lateinit var viewModel: SignUpPageViewModel
    private lateinit var handlerViewPager: Handler
    private lateinit var loginPreferences: SharedPreferences
    private var rememberLoginPreferences: Boolean = false
    private val callbackManager = CallbackManager.Factory.create()
//    val SAVE_PARAM_FILEPATH = "saveParamFilepath"
    val SAVE_PARAM_ISUSERREGISTERED = "isUserRegistered"
    private var username: String? = ""
    private var isUserRegistered: Int = 2

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
        reDesignGoogleButton(btn_google_login_sign_up_page, "\u0020     Gmail")

//        setupViewPager()
//        setupToolbar()
        setupEvents()
        subscribeToLiveData()
    }

    fun subscribeToLiveData() {
        viewModel.signUpResponse.observe(viewLifecycleOwner, Observer { voidResource ->
            when (voidResource?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showSnackbar(voidResource.message)
                }
                Status.SUCCESS -> {
                    viewModel.navigateToSelectUserType.call()
                }
            }
        })

        viewModel.navigateToSelectUserType.observe(viewLifecycleOwner, Observer {
            activity?.run {
                navigator.navigateToSelectUserTypeActivity(this)
                this.finish()
            }
        })

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

        viewModel.loginGoogleResponse.observe(this, Observer { response ->
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
            .requestIdToken("100096053983-f5nhf4j46i9bd5vh8u6c54juh2rn3gbc.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

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

//        btn_sign_up.setOnClickListener {
//            viewModel.loginEmail(sign_up_user_email.text.toString(), sign_up_user_password.text.toString())
//        }

        sign_up_user_password.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.loginEmail(sign_up_user_email.text.toString(), sign_up_user_password.text.toString())
                handled = true
            }
            handled
        }

//        // Remember password
//        checkbox_remember_password.setOnClickListener {
//            rememberLoginPreferences = checkbox_remember_password.isChecked()
//        }
//
//        loginPreferences = requireActivity().getSharedPreferences(
//            DIVERCITY_USERNAME_PASSWORD_PREF,
//            Context.MODE_PRIVATE
//        )
//        rememberLoginPreferences = loginPreferences.getBoolean(REMEMBER_USERNAME_PASSWORD, false)
//        if (rememberLoginPreferences) {
//            user_email.setText(loginPreferences.getString(USERNAME, null))
//            user_password.setText(loginPreferences.getString(PASSWORD, null))
//        }
//        checkbox_remember_password.setChecked(rememberLoginPreferences)
//
        // Sign up button
        btn_sign_up.isEnabled = false
        btn_sign_up.isClickable = false
        btn_sign_up.setBackgroundResource(R.drawable.shape_backgrd_round_blue1)

        // add listeners to email and password
        addListenerOnNameEmailAndPassword()
//
//        btn_sign_up.setOnClickListener {
//            var loginPreferencesEditor = loginPreferences.edit()
//            loginPreferencesEditor.putBoolean(REMEMBER_USERNAME_PASSWORD, rememberLoginPreferences)
//            if (rememberLoginPreferences) {
//                loginPreferencesEditor.putString(USERNAME, sign_up_user_email.getText().toString())
//                loginPreferencesEditor.putString(PASSWORD, sign_up_user_password.getText().toString())
//            }
//            loginPreferencesEditor.clear().commit()
//            viewModel.loginEmail(user_email.text.toString(), user_password.text.toString())
//        }

        btn_sign_up.setOnClickListener {
            if (checkFormIsCompleted())
                signUp()
            else
                showToast(getString(R.string.check_fields))
        }
    }

    private fun checkFormIsCompleted(): Boolean {
        return sign_up_user_name.text.toString() != "" &&
                sign_up_user_email.text.toString() != "" &&
                Util.isValidEmail(sign_up_user_email.text.toString()) &&
                sign_up_user_password.text.toString() != ""
    }

    private fun getTextEditText(editText: EditText): String {
        return editText.text.toString()
    }

    private fun signUp() {
        viewModel.signUp(
            Util.getNameFormatted(getTextEditText(sign_up_user_name)),
            getTextEditText(sign_up_user_email),
            getTextEditText(sign_up_user_password)
        )
    }


    fun getEdTxtEmail(): String {
        return user_email.text.toString().trim()
    }

    fun checkNameEmpty(): Boolean {
        return sign_up_user_name.text.toString().isEmpty()
    }

    fun checkEmailEmpty(): Boolean {
        return sign_up_user_email.text.toString().isEmpty()
    }

    fun checkPasswordEmpty(): Boolean {
        return sign_up_user_password.text.toString().isEmpty()
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
        if (requestCode == com.divercity.android.features.login.step1.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val token = account?.getIdToken()
                viewModel.loginGoogle(token)
                Log.d("TAG", "Google Token: " + token)
            } catch (e: ApiException) {
                Log.d("TAG", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private fun addListenerOnNameEmailAndPassword() {
        sign_up_user_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                signupButtonReactToSignupStatus()
            }
        })
        sign_up_user_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                signupButtonReactToSignupStatus()
            }
        })
        sign_up_user_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                signupButtonReactToSignupStatus()
            }
        })
    }

    private fun signupButtonReactToSignupStatus() {
        if (checkNameEmpty() || checkEmailEmpty() || checkPasswordEmpty())       {
            Log.d("LoginStatus", "email or password is empty")
            println("email or password is empty")
            btn_sign_up.isEnabled = false
            btn_sign_up.isClickable = false
            btn_sign_up.setBackgroundResource(R.drawable.shape_backgrd_round_blue1)
        } else {
            Log.d("LoginStatus", "email and password are filled")
            btn_sign_up.isEnabled = true
            btn_sign_up.isClickable = true
            btn_sign_up.setBackgroundResource(R.drawable.shape_backgrd_round_blue2)
        }
    }

    fun reDesignGoogleButton(signInButton: SignInButton, buttonText: String) {
        for (i in 0 until signInButton.childCount) {
            val v = signInButton.getChildAt(i)
            if (v is TextView) {
                v.text = buttonText //setup your text here
                v.setBackgroundResource(android.R.color.transparent) //setting transparent color that will hide google image and white background
                v.setTextColor(resources.getColor(R.color.grey)) // text color here
                v.typeface = Typeface.DEFAULT_BOLD // even typeface
                return
            }
        }
    }
}