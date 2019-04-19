package com.divercity.android.features.profile.potheruser

import android.os.Bundle
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
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.view_accept_decline.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class OtherUserProfileFragment : BaseFragment() {

    @Inject
    lateinit var adapter: OtherUserProfileViewPagerAdapter

    private lateinit var viewModel: OtherUserProfileViewModel

    companion object {

        private const val PARAM_USER_ID = "paramUserId"

        fun newInstance(userId: String?): OtherUserProfileFragment {
            val fragment = OtherUserProfileFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_USER_ID, userId)
            fragment.arguments = arguments
            return fragment
        }
    }

    enum class DataHolder {
        INSTANCE;

        private var userResponse: UserResponse? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.userResponse != null
            }

            var data: UserResponse?
                get() {
                    val retList = INSTANCE.userResponse
                    INSTANCE.userResponse = null
                    return retList
                }
                set(objectList) {
                    INSTANCE.userResponse = objectList
                }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_other_user_profile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(OtherUserProfileViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        if (DataHolder.hasData())
            viewModel.setUser(DataHolder.data!!)
        else
            viewModel.userId = arguments?.getString(PARAM_USER_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
        if (viewModel.user.value != null) {
            showData(viewModel.user.value!!)
        } else {
            viewModel.fetchProfileData()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    incl_profile.swipe_refresh_profile.isRefreshing = false
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    incl_profile.swipe_refresh_profile.isRefreshing = false
                    showData(response.data!!)
                }
            }
        })

        viewModel.acceptDeclineConnectionRequestResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
                    hideProgress()
                    include_accept_decline.visibility = View.GONE
                }
            }
        })
    }

    private fun showData(userResponse: UserResponse) {
        val attr = userResponse.userAttributes
        GlideApp.with(this)
            .load(attr?.avatarMedium)
            .apply(RequestOptions().circleCrop())
            .into(incl_profile.img_profile)

        incl_profile.txt_name.text = attr?.name
        incl_profile.txt_user_type.text =
            Util.getUserTypeMap(context!!)[userResponse.userAttributes?.accountType]

        if (attr?.connected == "pending_approval") {
            include_accept_decline.visibility = View.VISIBLE
            txt_notification.text = attr.name.plus(" wants to connect with you")
            btn_close.setOnClickListener {
                include_accept_decline.visibility = View.GONE
            }
            btn_accept.setOnClickListener {
                viewModel.acceptConnectionRequest(userResponse.id)
            }
            btn_decline.setOnClickListener {
                viewModel.declineConnectionRequest(userResponse.id)
            }
        } else {
            include_accept_decline.visibility = View.GONE
        }
    }

    private fun setupView() {
        adapter.userId = viewModel.userId!!
        incl_profile.viewPager.adapter = adapter
        incl_profile.tab_layout.setupWithViewPager(incl_profile.viewPager)

        incl_profile.swipe_refresh_profile.apply {
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
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.profile)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }
}