package com.divercity.app.features.login.step2

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
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

        fun newInstance(email : String): LoginFragment {
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
                it.setDisplayShowTitleEnabled(false)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        include_toolbar.title.text = resources.getString(R.string.login)
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
                    navigator.navigateToHomeActivity(activity!!)
                }
            }
        })
    }

    fun showSnackbar(message: String?) {
        activity?.run{
            Snackbar.make(
                    findViewById(android.R.id.content),
                    message ?: "Error",
                    Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun setupEvents() {
        txt_forgot_password.setOnClickListener { showToast() }
        btn_login.setOnClickListener {
            viewModel.login(et_password.text.toString())
        }
    }

    private fun showToast() {
        Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
    }
}