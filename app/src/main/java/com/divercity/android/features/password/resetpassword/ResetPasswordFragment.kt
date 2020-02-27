package com.divercity.android.features.password.resetpassword

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_log_in_page.*
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password_enter_email.*
//import kotlinx.android.synthetic.main.fragment_reset_password_enter_email.user_email
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class ResetPasswordFragment : BaseFragment() {

    private lateinit var viewModel: ResetPasswordViewModel

    companion object {
        private const val PARAM_TOKEN = "paramToken"

        fun newInstance(token: String): ResetPasswordFragment {
            val fragment =
                ResetPasswordFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_TOKEN, token)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_reset_password_enter_email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ResetPasswordViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

//    private fun setupToolbar() {
//        (activity as AppCompatActivity).apply {
////            setSupportActionBar(include_toolbar.toolbar)
//            supportActionBar?.let {
//                it.setTitle(R.string.reset_password)
//                if (!isTaskRoot)
//                    it.setDisplayHomeAsUpEnabled(true)
//            }
//        }
//    }

    fun setupView() {
//        btn_continue.setOnClickListener {
//            val newPassword = et_new_password.text.toString()
//            val confirmedPassword = et_confirm_new_pass.text.toString()
//
//            if (newPassword.isEmpty() || confirmedPassword.isEmpty() || newPassword != confirmedPassword)
//                showToast("Check Passwords")
//            else
//                viewModel.resetPassword(newPassword, arguments?.getString(PARAM_TOKEN)!!)
//        }

        // Login button
        btn_send_reset_pw_request.isEnabled = false
        btn_send_reset_pw_request.isClickable = false
        btn_send_reset_pw_request.setBackgroundResource(R.drawable.shape_backgrd_round_blue1)

        // add listeners to email
        addListenerOnEmail()

        btn_send_reset_pw_request.setOnClickListener {
            viewModel.requestResetPassword(reset_pw_user_email.text.toString())
        }
    }

    fun subscribeToLiveData() {
        viewModel.resetPasswordResponse.observe(viewLifecycleOwner, Observer { voidResource ->
            when (voidResource?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showToast(voidResource.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast("Password Changed Successfully")
                    if (requireActivity().isTaskRoot)
                        navigator.navigateToSplash(this)
                    requireActivity().finish()
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
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireContext().applicationContext, msg, Toast.LENGTH_SHORT).show()
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

    fun showSnackbar(message: String?) {
        activity?.run {
            Snackbar.make(
                findViewById(android.R.id.content),
                message ?: "Error",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    fun checkEmailEmpty(): Boolean {
        return reset_pw_user_email.text.toString().isEmpty()
    }

    private fun resetPasswordRequestButtonReactToResetStatus() {
        if (checkEmailEmpty()) {
            Log.d("LoginStatus", "email is empty")
            println("email is empty")
            btn_send_reset_pw_request.isEnabled = false
            btn_send_reset_pw_request.isClickable = false
            btn_send_reset_pw_request.setBackgroundResource(R.drawable.shape_backgrd_round_blue1)
        } else {
            Log.d("LoginStatus", "email is filled")
            btn_send_reset_pw_request.isEnabled = true
            btn_send_reset_pw_request.isClickable = true
            btn_send_reset_pw_request.setBackgroundResource(R.drawable.shape_backgrd_round_blue2)
        }
    }

    private fun addListenerOnEmail() {
        reset_pw_user_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                resetPasswordRequestButtonReactToResetStatus()
            }
        })
    }
}