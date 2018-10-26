package com.divercity.app.features.signup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.Toast
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class SignUpFragment : BaseFragment() {

    lateinit var viewModel: SignUpViewModel

    companion object {
        private const val PARAM_EMAIL = "paramEmail"

        fun newInstance(email : String): SignUpFragment {
            val fragment = SignUpFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_EMAIL, email)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_sign_up

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        setupToolbar()
        setupEvents()
        subscribeToLiveData()

        et_email.setText(arguments?.getString("email") ?: "")
    }


    private fun setupToolbar() {
        (activity as SignUpActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayShowTitleEnabled(false)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
        include_toolbar.title.text = resources.getString(R.string.account_profile)
    }

    fun subscribeToLiveData() {
        viewModel.signUp.observe(this, Observer { voidResource ->
            when (voidResource?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showSnackbar(voidResource.message)
                }
                Status.SUCCESS -> {
                    hideProgress()
                    //navigateHomeActivity
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
        photo.setOnClickListener { showToast() }
    }

    private fun showToast() {
        Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show()
    }
}