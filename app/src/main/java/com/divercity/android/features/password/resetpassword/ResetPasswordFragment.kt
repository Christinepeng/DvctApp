package com.divercity.android.features.password.resetpassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import kotlinx.android.synthetic.main.fragment_reset_password.*
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

    override fun layoutId(): Int = R.layout.fragment_reset_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ResetPasswordViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.reset_password)
                if (!isTaskRoot)
                    it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    fun setupView() {
        btn_continue.setOnClickListener {
            val newPassword = et_new_password.text.toString()
            val confirmedPassword = et_confirm_new_pass.text.toString()

            if (newPassword.isEmpty() || confirmedPassword.isEmpty() || newPassword != confirmedPassword)
                showToast("Check Passwords")
            else
                viewModel.resetPassword(newPassword, arguments?.getString(PARAM_TOKEN)!!)
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
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireContext().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}