package com.divercity.android.features.user.profileotheruser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import com.divercity.android.features.dialogs.picture.PictureDialogFragment
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.fragment_other_user_profile.*
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

        private var userResponse: User? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.userResponse != null
            }

            var data: User?
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

        if (DataHolder.hasData()) {
            viewModel.setUser(DataHolder.data!!)
        } else {
            viewModel.userId = arguments?.getString(PARAM_USER_ID)!!
            viewModel.fetchUserData()
        }
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
                    if (lay_root.visibility == View.GONE)
                        showDialogConnectionError()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> {
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

        viewModel.connectUserResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })

        viewModel.userLiveData.observe(viewLifecycleOwner, Observer { user ->
            showData(user)
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showData(user: User?) {
        user?.let {
            lay_root.visibility = View.VISIBLE

            GlideApp.with(this)
                .load(it.avatarMedium)
                .apply(RequestOptions().circleCrop())
                .into(img_profile)

            img_profile.setOnClickListener {
                user.avatarMedium?.let { url ->
                    showPictureDialog(url)
                }
            }

            txt_name.text = user.name
            txt_user_type.text =
                Util.getUserTypeMap(requireContext())[it.accountType]

            if (!it.ethnicity.isNullOrBlank()) {
                img_ethnicity.visibility = View.VISIBLE
                txt_ethnicity.visibility = View.VISIBLE
                txt_ethnicity.text = it.ethnicity
            } else {
                img_ethnicity.visibility = View.GONE
                txt_ethnicity.visibility = View.GONE
            }

            if (!it.gender.isNullOrBlank()) {
                img_gender.visibility = View.VISIBLE
                txt_gender.visibility = View.VISIBLE
                txt_gender.text = it.gender
            } else {
                img_gender.visibility = View.GONE
                txt_gender.visibility = View.GONE
            }

            if (!it.ageRange.isNullOrBlank()) {
                img_age_range.visibility = View.VISIBLE
                txt_age_range.visibility = View.VISIBLE
                txt_age_range.text = it.ageRange
            } else {
                img_age_range.visibility = View.GONE
                txt_age_range.visibility = View.GONE
            }

            if (!it.fullLocation().isNullOrBlank()) {
                img_location.visibility = View.VISIBLE
                txt_location.visibility = View.VISIBLE
                txt_location.text = it.fullLocation()
            } else {
                img_location.visibility = View.GONE
                txt_location.visibility = View.GONE
            }

            when (user.connected) {
                "connected" -> {
                    include_accept_decline.visibility = View.GONE

                    btn_connect.setImageResource(R.drawable.icon_connected)
                    btn_connect.setOnClickListener(null)
                }
                "requested" -> {
                    include_accept_decline.visibility = View.GONE

                    btn_connect.setImageResource(R.drawable.icon_followed)
                    btn_connect.setOnClickListener(null)
                }
                "pending_approval" -> {
                    include_accept_decline.visibility = View.VISIBLE
                    txt_notification.text = user.name.plus(" wants to connect with you")
                    btn_close.setOnClickListener {
                        include_accept_decline.visibility = View.GONE
                    }
                    btn_accept.setOnClickListener {
                        viewModel.acceptConnectionRequest(user.id)
                    }
                    btn_decline.setOnClickListener {
                        viewModel.declineConnectionRequest(user.id)
                    }


                    btn_connect.setOnClickListener {
                        viewModel.acceptConnectionRequest(user.id)
                    }
                    btn_connect.setImageResource(R.drawable.icon_pending_approval)
                    btn_connect.setOnClickListener(null)
                }
                "not_connected" -> {
                    btn_connect.setImageResource(R.drawable.icon_request_connection)
                    btn_connect.isEnabled = true
                    include_accept_decline.visibility = View.GONE

                    btn_connect.setOnClickListener {
                        viewModel.connectToUser()
                    }
                }
            }
        }
    }

    private fun setupView() {
        group_connect_dm.visibility = View.VISIBLE

        btn_dm.setOnClickListener {
            navigator.navigateToChatActivity(
                this@OtherUserProfileFragment,
                viewModel.userLiveData.value?.name!!,
                viewModel.userId!!,
                null
            )
        }

        adapter.userId = viewModel.userId!!
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)
    }

    private fun showDialogConnectionError() {
        val dialog = CustomTwoBtnDialogFragment.newInstance(
            getString(R.string.ups),
            getString(R.string.error_connection),
            getString(R.string.cancel),
            getString(R.string.retry)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                viewModel.fetchUserData()
            }

            override fun onPositiveBtnClick() {
                requireActivity().finish()
            }
        })
        dialog.isCancelable = false
        dialog.show(childFragmentManager, null)
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

    private fun showPictureDialog(url: String) {
        val dialog = PictureDialogFragment.newInstance(url)
        dialog.show(childFragmentManager, null)
    }
}