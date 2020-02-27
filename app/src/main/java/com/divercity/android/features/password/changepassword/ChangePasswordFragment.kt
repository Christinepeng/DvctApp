package com.divercity.android.features.password.changepassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class ChangePasswordFragment : BaseFragment() {

    private lateinit var viewModel: ChangePasswordViewModel

    companion object {

        fun newInstance(): ChangePasswordFragment {
            return ChangePasswordFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_change_password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[ChangePasswordViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

//    private fun setupToolbar() {
//        (activity as AppCompatActivity).apply {
//            setSupportActionBar(include_toolbar.toolbar)
//            supportActionBar?.let {
//                it.setTitle(R.string.change_password)
//                it.setDisplayHomeAsUpEnabled(true)
//            }
//        }
//    }

    fun setupView() {
        btn_create_password.setOnClickListener {
//            val oldPassword = et_old_password.text.toString()
            val newPassword = et_new_password.text.toString()
            val confirmedPassword = et_confirm_new_pass.text.toString()

            if (newPassword.isEmpty() ||
                confirmedPassword.isEmpty() ||
                newPassword != confirmedPassword
//                oldPassword.isEmpty()
            )
                showToast(getString(R.string.check_fields))
            else
                viewModel.changePassword(newPassword, confirmedPassword)
        }
    }

    fun subscribeToLiveData() {
        viewModel.changePasswordResponse.observe(viewLifecycleOwner, Observer { voidResource ->
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
                    requireActivity().finish()
                }
            }
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(requireContext().applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}