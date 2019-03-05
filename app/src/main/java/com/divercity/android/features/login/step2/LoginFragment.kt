package com.divercity.android.features.login.step2

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class LoginFragment : BaseFragment() {

    lateinit var viewModel: LoginViewModel

    companion object {
        private const val PARAM_EMAIL = "paramEmail"

        fun newInstance(email: String): LoginFragment {
            val fragment = LoginFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_EMAIL, email)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[LoginViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setUserEmail(arguments?.getString(PARAM_EMAIL))
        hideKeyboard()
        setupToolbar()
        setupEvents()
        subscribeToLiveData()
    }


    private fun setupToolbar() {
        (activity as LoginActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.login)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    fun subscribeToLiveData() {
        viewModel.login.observe(this, Observer { response ->
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
                    if (response.data?.userAttributes?.accountType == null)
                        navigator.navigateToSelectUserTypeActivity(activity!!)
                    else
                        navigator.navigateToHomeActivity(activity!!)
                }
            }
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
        txt_forgot_password.setOnClickListener { showToast() }
        btn_create_account.setOnClickListener {
            viewModel.login(et_password.text.toString())
        }

        et_password.setOnEditorActionListener { _, i, _ ->
            var handled = false
            if (i == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(et_password.text.toString())
                handled = true
            }
            handled
        }
    }

    private fun showToast() {
        Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
    }
}