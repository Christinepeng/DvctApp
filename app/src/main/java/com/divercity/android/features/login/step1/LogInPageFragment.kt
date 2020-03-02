package com.divercity.android.features.login.step1

import android.content.Context.MODE_PRIVATE
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
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * Created by lucas on 24/10/2018.
 */

const val RC_SIGN_IN = 123
const val DIVERCITY_USERNAME_PASSWORD_PREF = "divercity_username_password_pref"
const val REMEMBER_USERNAME_PASSWORD = "remember_username_password"
const val USERNAME = "username"
const val PASSWORD = "password"

class LogInPageFragment : BaseFragment() {

    lateinit var viewModel: LogInPageViewModel
    private lateinit var handlerViewPager: Handler
    private lateinit var loginPreferences: SharedPreferences
    private val callbackManager = CallbackManager.Factory.create()
    private var rememberLoginPreferences: Boolean = false
    private var emailFocused: Boolean = false

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handlerViewPager = Handler()
        hideKeyboard()
        reDesignGoogleButton(btn_google_login, "\u0020     Gmail")
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
                }
                Status.SUCCESS -> {
                    hideProgress()
                    if (response.data == true) {
                        icon_green_check_mark.visibility = View.VISIBLE
                    } else {
                        icon_green_check_mark.visibility = View.INVISIBLE
                    }
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

//        viewModel.navigateToLogin.observe(this, Observer {
//            navigator.navigateToLoginActivity(requireActivity(), getEdTxtEmail())
//        })
//
//        viewModel.navigateToSignUp.observe(this, Observer {
//            navigator.navigateToSignUpActivity(requireActivity(), getEdTxtEmail())
//        })
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

        btn_google_login.setOnClickListener {
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

        btn_go_to_sign_up.setOnClickListener {
            navigator.navigateToSignUpPageFragment(requireActivity())
        }

        btn_forget_password.setOnClickListener {
            navigator.navigateToResetPassword(requireActivity(), user_password.text.toString())
//            viewModel.requestResetPassword(user_email.text.toString())
        }

        btn_log_in.setOnClickListener {
            viewModel.loginEmail(user_email.text.toString(), user_password.text.toString())
        }

        // Remember password
        checkbox_remember_password.setOnClickListener {
            rememberLoginPreferences = checkbox_remember_password.isChecked()
        }

        loginPreferences = requireActivity().getSharedPreferences(DIVERCITY_USERNAME_PASSWORD_PREF, MODE_PRIVATE)
        rememberLoginPreferences = loginPreferences.getBoolean(REMEMBER_USERNAME_PASSWORD, false)
        if (rememberLoginPreferences) {
            user_email.setText(loginPreferences.getString(USERNAME, null))
            user_password.setText(loginPreferences.getString(PASSWORD, null))
        }
        checkbox_remember_password.setChecked(rememberLoginPreferences)

        // Login button
        btn_log_in.isEnabled = false
        btn_log_in.isClickable = false
        btn_log_in.setBackgroundResource(R.drawable.shape_backgrd_round_blue1)

        // add listeners to email and password
        addListenerOnEmailAndPassword()

        btn_log_in.setOnClickListener {
            var loginPreferencesEditor = loginPreferences.edit()
            loginPreferencesEditor.putBoolean(REMEMBER_USERNAME_PASSWORD, rememberLoginPreferences)
            if (rememberLoginPreferences) {
                loginPreferencesEditor.putString(USERNAME, user_email.getText().toString())
                loginPreferencesEditor.putString(PASSWORD, user_password.getText().toString())
            }
            loginPreferencesEditor.clear().commit()
            viewModel.loginEmail(user_email.text.toString(), user_password.text.toString())
        }

        // Additionally check the email/password fields in case that they are remembered
        loginButtonReactToLoginStatus()
    }

//    fun checkEmailValid() : Boolean {
//        return Util.isValidEmail(user_email.text.toString().trim() { it <= ' ' })
//    }
//
////    fun checkIfEmailRegistered() : Boolean {
////        return
////    }

    fun getEdTxtEmail(): String {
        return user_email.text.toString().trim()
    }

    fun checkEmailEmpty(): Boolean {
        return user_email.text.toString().isEmpty()
    }

    fun checkPasswordEmpty(): Boolean {
        return user_password.text.toString().isEmpty()
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

    private fun showRequireValidEmailDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.divercity),
            getString(R.string.input_valid_email),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener { }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private fun showPasswordTooShortDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.divercity),
            getString(R.string.password_too_short),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener { }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private fun showEmailIsNotUsedYetDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.divercity),
            getString(R.string.email_is_not_used_yet),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener { }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private fun showEmailIsAlreadyUsedDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.divercity),
            getString(R.string.email_is_already_used),
            getString(R.string.ok)
        )
        customOneBtnDialogFragment.setListener { }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    private fun showInvalidLoginCredentialsDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            getString(R.string.divercity),
            getString(R.string.invalid_login_credentials),
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
                val account = task.getResult(ApiException::class.java)
                val token = account?.getIdToken()
                viewModel.loginGoogle(token)
                Log.d("TAG", "Google Token: " + token)
            } catch (e: ApiException) {
                Log.d("TAG", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }

    private fun addListenerOnEmailAndPassword() {
        user_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginButtonReactToLoginStatus()
            }
        })
        user_email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailFocused = true
            } else {
                if (emailFocused) {
                    viewModel.checkIfEmailRegistered(user_email.text.toString())
                }
                emailFocused = false
            }
        }
        user_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                loginButtonReactToLoginStatus()
            }
        })
    }

    private fun loginButtonReactToLoginStatus() {
        if (checkEmailEmpty() || checkPasswordEmpty())       {
            Log.d("LoginStatus", "email or password is empty")
            println("email or password is empty")
            btn_log_in.isEnabled = false
            btn_log_in.isClickable = false
            btn_log_in.setBackgroundResource(R.drawable.shape_backgrd_round_blue1)
        } else {
            Log.d("LoginStatus", "email and password are filled")
            btn_log_in.isEnabled = true
            btn_log_in.isClickable = true
            btn_log_in.setBackgroundResource(R.drawable.shape_backgrd_round_blue2)
        }
    }

//    private fun checkInputEmail() {
//        if (!checkEmailEmpty() && !checkEmailValid()) {
//            showRequireValidEmailDialog()
//        } else {
////            if (!checkIfEmailRegistered()) {
////                showEmailIsNotUsedYetDialog()
////            } else {
////                icon_green_check_mark.visibility = View.VISIBLE
////            }
//        }
//    }

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