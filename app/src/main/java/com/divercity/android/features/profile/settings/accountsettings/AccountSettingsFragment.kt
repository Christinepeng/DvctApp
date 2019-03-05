package com.divercity.android.features.profile.settings.accountsettings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_account_settings.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 24/10/2018.
 */

class AccountSettingsFragment : BaseFragment() {

    private lateinit var viewModel: AccountSettingsViewModel

    companion object {

        fun newInstance(): AccountSettingsFragment {
            return AccountSettingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_account_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[AccountSettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        (activity as AccountSettingsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.account_settings)
            }
        }
    }

    private fun setupView(){
        txt_name.text = viewModel.getName()
        txt_email.text = viewModel.getEmail()
    }
}