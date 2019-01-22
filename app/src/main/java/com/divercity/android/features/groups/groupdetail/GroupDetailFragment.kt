package com.divercity.android.features.groups.groupdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.GroupResponse
import com.divercity.android.data.entity.user.response.UserResponse
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.view_image_with_foreground.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class GroupDetailFragment : BaseFragment() {

    lateinit var viewModel: GroupDetailViewModel

    @Inject
    lateinit var adapter: GroupDetailViewPagerAdapter

    private var group: GroupResponse? = null

    companion object {

        private const val PARAM_GROUP = "paramGroup"

        fun newInstance(group: GroupResponse): GroupDetailFragment {
            val fragment = GroupDetailFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_GROUP, group)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_group_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[GroupDetailViewModel::class.java]
        setHasOptionsMenu(true)
        group = arguments?.getParcelable(PARAM_GROUP)
        group?.let {
            viewModel.fetchGroupMembers(it, 0, 5, null)
        }
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
                it.setTitle(R.string.groups)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupView() {
        adapter.group = group!!
        viewPager.adapter = adapter
        tab_layout.setupWithViewPager(viewPager)

        group?.let {
            item_txt_name.text = it.attributes?.title
            if (it.isPublic)
                item_txt_detail.text =
                        "Public Group · ".plus(it.attributes?.followersCount).plus(" Members")
            else
                item_txt_detail.text =
                        "Private Group · ".plus(it.attributes?.followersCount).plus(" Members")

            GlideApp.with(this)
                .load(it.attributes.pictureMain)
                .into(img_default_group)

            if(it.attributes.isIsFollowedByCurrent){
                btn_member_pending.setText(R.string.member)
                btn_member_pending.visibility = View.VISIBLE
                btn_conversation.visibility = View.VISIBLE
            } else if(it.isPublic){
                btn_join.visibility = View.VISIBLE
                btn_join.setOnClickListener {
                    viewModel.joinGroup(group!!)
                }
            } else if(it.isJoinRequestNotSend){
                btn_join.setText(R.string.request_to_join)
                btn_join.visibility = View.VISIBLE
                btn_join.setOnClickListener {
                    viewModel.requestToJoinGroup(group!!)
                }
            } else if(it.isJoinRequestPending){
                btn_member_pending.setText(R.string.pending)
                btn_member_pending.visibility = View.VISIBLE
            }
        }
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

        viewModel.joinGroupResponse.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { group ->
                when (group.status) {
                    Status.LOADING -> showProgress()

                    Status.ERROR -> {
                        hideProgress()
                        Toast.makeText(activity, group.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        hideProgress()
                        btn_join.visibility = View.GONE
                        btn_member_pending.setText(R.string.member)
                        btn_member_pending.visibility = View.VISIBLE
                        btn_conversation.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.requestToJoinResponse.observe(viewLifecycleOwner, Observer {response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    btn_join.visibility = View.GONE
                    btn_member_pending.setText(R.string.pending)
                    btn_member_pending.visibility = View.VISIBLE
                    btn_conversation.visibility = View.GONE
                }
            }
        })
    }

    private fun showMemberPictures(members: List<UserResponse>) {
        val imgViews: Array<View> =
            arrayOf(lay_img1, lay_img2, lay_img3, lay_img4, lay_img5)

        for(i in members.indices){
            imgViews[i].visibility = View.VISIBLE

            GlideApp.with(this)
                    .load(members[i].userAttributes?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(imgViews[i].img)

            if(i == members.size - 1 && i != group?.attributes?.followersCount!! - 1)
                (imgViews[i] as FrameLayout).foreground = ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_circular)        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search_threedots, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                menu.findItem(R.id.action).isVisible = false
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                menu.findItem(R.id.action).isVisible = true
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }
}