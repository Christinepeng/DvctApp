package com.divercity.android.features.groups.groupdetail.about

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.fragment_tab_about_group_detail.*
import kotlinx.android.synthetic.main.view_image_with_foreground.view.*

/**
 * Created by lucas on 16/11/2018.
 */

class TabAboutGroupDetailFragment : BaseFragment() {

    private lateinit var viewModel: TabAboutGroupDetailViewModel

    private lateinit var group: GroupResponse

    companion object {

        private const val PARAM_GROUP = "paramGroup"

        fun newInstance(group: GroupResponse): TabAboutGroupDetailFragment {
            val fragment = TabAboutGroupDetailFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_GROUP, group)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_tab_about_group_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory)
                .get(TabAboutGroupDetailViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")
        group = arguments!!.getParcelable(PARAM_GROUP)!!
        group.let {
            viewModel.fetchGroupMembers(it, 0, 8, null)
            viewModel.fetchGroupAdmins(it, 0, 8, null)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        subscribeToLiveData()
    }

    fun setupView() {
        if (group?.attributes?.description != null) {
            txt_description.hint = group?.attributes?.description
            txt_description.visibility = View.VISIBLE
        } else {
            txt_description.visibility = View.GONE
        }

        if (group!!.isPublic()) {
            img_lock_unlock.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.img_unlock
                )
            )
            txt_type.text = getString(R.string.public_group)
        } else {
            img_lock_unlock.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.img_lock
                )
            )
            txt_type.text = getString(R.string.private_group)
        }

        txt_members.hint = (group?.attributes?.followersCount).toString().plus(" Members")
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun subscribeToLiveData() {
        viewModel.fetchGroupMembersResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showMemberPictures(response.data!!)
                }
            }
        })

        viewModel.fetchGroupAdminsResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showAdminPictures(response.data!!)
                }
            }
        })
    }

    private fun showMemberPictures(members: List<UserResponse>) {
        lay_pictures.visibility = View.VISIBLE

        val imgViews: Array<View> =
            arrayOf(lay_img1, lay_img2, lay_img3, lay_img4, lay_img5, lay_img6, lay_img7, lay_img8)

        for (i in members.indices) {
            imgViews[i].visibility = View.VISIBLE

            GlideApp.with(this)
                .load(members[i].userAttributes?.avatarThumb)
                .apply(RequestOptions().circleCrop())
                .into(imgViews[i].img)

            if (i == members.size - 1 && i != group?.attributes?.followersCount!! - 1)
                (imgViews[i] as FrameLayout).foreground =
                    ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_circular)
        }
    }

    private fun showAdminPictures(admins: List<UserResponse>) {
        if (admins.isNotEmpty()) {
            lbl_admins.visibility = View.VISIBLE
            lay_admin.visibility = View.VISIBLE
            lay_admin_img.visibility = View.VISIBLE

            val imgViews: Array<View> =
                arrayOf(
                    lay_adm_img1,
                    lay_adm_img2,
                    lay_adm_img3,
                    lay_adm_img4,
                    lay_adm_img5,
                    lay_adm_img6,
                    lay_adm_img7,
                    lay_adm_img8
                )

            for (i in admins.indices) {
                imgViews[i].visibility = View.VISIBLE

                GlideApp.with(this)
                    .load(admins[i].userAttributes?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(imgViews[i].img)

//                if (i == admins.size - 1)
//                    (imgViews[i] as FrameLayout).foreground = ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_circular)

                if (i == 0)
                    txt_admins.hint = admins[i].userAttributes?.name
                else
                    txt_admins.hint =
                        txt_admins.hint.toString().plus(",").plus(admins[i].userAttributes?.name)
            }
        }
    }
}