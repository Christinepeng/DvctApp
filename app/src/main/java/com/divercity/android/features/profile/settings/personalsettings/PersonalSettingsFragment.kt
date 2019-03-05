package com.divercity.android.features.profile.settings.personalsettings

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.profile.tabprofile.TabProfileFragment
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import java.io.File

/**
 * Created by lucas on 24/10/2018.
 */

class PersonalSettingsFragment : BaseFragment() {

    val SAVE_PARAM_FILEPATH = "saveParamFilepath"
    val SAVE_PARAM_USERNAME = "username"
    val SAVE_PARAM_ISUSERREGISTERED = "isUserRegistered"

    private var photoFile: File? = null
    private var isPictureSet: Boolean = false

    private lateinit var viewModel: PersonalSettingsViewModel
    private var username: String? = ""
    private var isUserRegistered: Int = 2

    companion object {

        fun newInstance(): PersonalSettingsFragment {
            return PersonalSettingsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_personal_settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[PersonalSettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
            R.id.fragment_container, TabProfileFragment.newInstance()).commit()
        setupToolbar()
    }

    private fun setupToolbar() {
        (activity as PersonalSettingsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.settings)
            }
        }
    }
}