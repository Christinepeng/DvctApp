package com.divercity.android.features.profile

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var adapter: ProfileViewPagerAdapter

    private lateinit var viewModel: ProfileViewModel

    lateinit var handlerViewPager: Handler

    companion object {

        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun layoutId(): Int = R.layout.fragment_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setupToolbar()

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    showData(response.data!!)
                }
            }
        })
    }

    private fun showData(userResponse: UserResponse) {
        val attr = userResponse.userAttributes
        GlideApp.with(this)
            .load(attr?.avatarMedium)
            .apply(RequestOptions().circleCrop())
            .into(img_profile)

        txt_name.text = attr?.name
        txt_user_type.text = Util.getUserTypeMap(context!!)[userResponse.userAttributes?.accountType]

//        txt_user_track.text = attr?.followersCount.toString()
//            .plus(" Followers \u00B7 ")
//            .plus(attr?.followingCount)
//            .plus(" Following \u00B7 ")
//            .plus(attr?.groupOfInterestFollowingCount)
//            .plus(" Groups")
    }

    private fun setupView() {
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
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