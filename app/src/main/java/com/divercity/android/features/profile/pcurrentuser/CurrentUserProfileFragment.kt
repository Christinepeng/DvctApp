package com.divercity.android.features.profile.pcurrentuser

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class CurrentUserProfileFragment : BaseFragment() {

    @Inject
    lateinit var adapter: ProfileViewPagerAdapter

    private lateinit var viewModel: CurrentUserProfileViewModel

    companion object {

        fun newInstance(): CurrentUserProfileFragment =
            CurrentUserProfileFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CurrentUserProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    swipe_refresh_profile.isRefreshing = false
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    swipe_refresh_profile.isRefreshing = false
                }
            }
        })

        viewModel.getCurrentUser().observe(viewLifecycleOwner, Observer {
            showData(it)
        })
    }

    private fun showData(userResponse: UserResponse) {
        val attr = userResponse.userAttributes
        GlideApp.with(this)
            .load(attr?.avatarMedium)
            .apply(RequestOptions().circleCrop())
            .into(img_profile)

        txt_name.text = attr?.name
        txt_user_type.text =
            Util.getUserTypeMap(context!!)[userResponse.userAttributes?.accountType]
    }

    private fun setupView() {
        adapter.userId = viewModel.getCurrentUserId()
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)

        swipe_refresh_profile.apply {
            setOnRefreshListener {
                viewModel.fetchProfileData()
            }
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimaryDark),
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            icon_notification.visibility = View.GONE
            supportActionBar?.let {
                it.setTitle(R.string.profile)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_setting -> {
                navigator.navigateToProfileSettingsActivity(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}