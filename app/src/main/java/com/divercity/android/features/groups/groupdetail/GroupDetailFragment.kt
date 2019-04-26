package com.divercity.android.features.groups.groupdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.group.group.GroupResponse
import com.divercity.android.features.dialogs.groupaction.GroupAdminActionsDialogFragment
import com.divercity.android.features.dialogs.groupaction.GroupMemberActionsDialogFragment
import com.divercity.android.features.dialogs.invitegroup.InviteGroupDialogFragment
import com.divercity.android.features.invitations.contacts.InvitePhoneContactsActivity
import com.divercity.android.features.invitations.users.InviteUsersActivity
import com.divercity.android.model.user.User
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.view_accept_decline.*
import kotlinx.android.synthetic.main.view_image_with_foreground.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class GroupDetailFragment : BaseFragment(), InviteGroupDialogFragment.Listener {

    lateinit var viewModel: GroupDetailViewModel

    @Inject
    lateinit var adapter: GroupDetailViewPagerAdapter

    private lateinit var groupId: String

    companion object {

        private const val PARAM_GROUP_ID = "paramGroupId"

        fun newInstance(groupId: String): GroupDetailFragment {
            val fragment = GroupDetailFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_GROUP_ID, groupId)
            fragment.arguments = arguments
            return fragment
        }
    }

    enum class DataHolder {
        INSTANCE;

        private var group: GroupResponse? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.group != null
            }

            var data: GroupResponse?
                get() {
                    val groupResponse = INSTANCE.group
                    INSTANCE.group = null
                    return groupResponse
                }
                set(objectList) {
                    INSTANCE.group = objectList
                }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_group_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        groupId = arguments?.getString(PARAM_GROUP_ID)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[GroupDetailViewModel::class.java]
        setupToolbar()
        subscribeToLiveData()

        if (DataHolder.hasData()) {
            viewModel.groupLiveData.postValue(DataHolder.data)
        } else if (viewModel.groupLiveData.value == null)
            viewModel.fetchGroupById(groupId)
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

    private fun setupView(group: GroupResponse?) {
        if (group != null) {
            viewModel.fetchGroupMembers(groupId, 0, 5, null)

            adapter.group = group
            viewPager.adapter = adapter
            tab_layout.setupWithViewPager(viewPager)

            item_txt_name.text = group.attributes.title
            if (group.isPublic())
                item_txt_detail.text =
                    "Public Group · ".plus(group.attributes.followersCount).plus(" Members")
            else
                item_txt_detail.text =
                    "Private Group · ".plus(group.attributes.followersCount).plus(" Members")

            GlideApp.with(this)
                .load(group.attributes.pictureMain)
                .into(img_default_group)

            btn_fab.setOnClickListener {
                navigator.navigateToCreateTopicActivity(this@GroupDetailFragment, group)
//                viewModel.fetchGroupById(groupId)
            }

            if (group.attributes.groupAdminInvite?.inviteId != null) {
                include_accept_decline.visibility = View.VISIBLE

                txt_notification.text = "You have been added as an admin to this group"
                btn_close.setOnClickListener {
                    include_accept_decline.visibility = View.GONE
                }
                btn_accept.setOnClickListener {
                    viewModel.acceptGroupAdminInvite(group.attributes.groupAdminInvite?.inviteId!!)
                }
                btn_decline.setOnClickListener {
                    viewModel.declineGroupAdminInvite(group.attributes.groupAdminInvite?.inviteId!!)
                }

            } else {
                include_accept_decline.visibility = View.GONE
            }

            if (group.attributes.isCurrentUserAdmin) {
                btn_member_pending.setText(R.string.admin)
                btn_member_pending.visibility = View.VISIBLE
                btn_conversation.visibility = View.VISIBLE
                (btn_fab as View).visibility = View.VISIBLE
                btn_join.visibility = View.GONE
            } else if (group.attributes.isFollowedByCurrent) {
                btn_member_pending.setText(R.string.member)
                btn_member_pending.visibility = View.VISIBLE
                btn_conversation.visibility = View.VISIBLE
                (btn_fab as View).visibility = View.VISIBLE
                btn_join.visibility = View.GONE
            } else if (group.isPublic()) {
                btn_join.visibility = View.VISIBLE
                btn_join.setOnClickListener { viewModel.joinGroup(group) }
                (btn_fab as View).visibility = View.GONE
            } else if (group.isJoinRequestNotSend()) {
                btn_member_pending.visibility = View.GONE
                btn_conversation.visibility = View.GONE
                btn_join.setText(R.string.request_to_join)
                btn_join.visibility = View.VISIBLE
                btn_join.setOnClickListener { viewModel.requestToJoinGroup(group) }
                (btn_fab as View).visibility = View.GONE
            } else if (group.isJoinRequestPending()) {
                btn_member_pending.setText(R.string.pending)
                btn_member_pending.visibility = View.VISIBLE
                btn_conversation.visibility = View.GONE
                btn_join.visibility = View.GONE
                (btn_fab as View).visibility = View.GONE
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.groupLiveData.observe(viewLifecycleOwner, Observer { group ->
            setupView(group)
        })

        viewModel.fetchGroupMembersResponse.observe(viewLifecycleOwner, Observer { response ->
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

        viewModel.fetchGroupByIdResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                }
            }
        })

        viewModel.groupAdminInviteResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    }
                }
            }
        })

        viewModel.requestToJoinResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })
    }

    private fun showMemberPictures(members: List<User>) {
        val imgViews: Array<View> =
            arrayOf(lay_img1, lay_img2, lay_img3, lay_img4, lay_img5)

        for (i in members.indices) {
            imgViews[i].visibility = View.VISIBLE

            GlideApp.with(this)
                .load(members[i].avatarThumb)
                .apply(RequestOptions().circleCrop())
                .into(imgViews[i].img)

            if (i == members.size - 1 && i != viewModel.getGroup()?.attributes?.followersCount!! - 1)
                (imgViews[i] as FrameLayout).foreground =
                    ContextCompat.getDrawable(context!!, R.drawable.shape_backgrd_circular)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action -> {
                val group = viewModel.getGroup()
                if (group != null) {
                    if (group.attributes.isCurrentUserAdmin)
                        showGroupAdminActionsDialog()
                    else if (group.attributes.isFollowedByCurrent)
                        showGroupMemberActionsDialog()
                }
                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showGroupAdminActionsDialog() {
        val fragment = GroupAdminActionsDialogFragment.newInstance()
        fragment.listener = object : GroupAdminActionsDialogFragment.Listener {

            override fun onEditAdmin() {
                navigator.navigateToDeleteGroupAdmin(
                    this@GroupDetailFragment,
                    groupId,
                    viewModel.getGroup()?.attributes?.authorInfo?.id!!.toString()
                )
            }

            override fun onEditMembers() {

            }

            override fun onInvite() {
                showInviteGroupDialog()
            }

            override fun onEditGroup() {
                navigator.navigateToEditGroupStep1(this@GroupDetailFragment, viewModel.getGroup())
            }

            override fun onAddAdmin() {
                navigator.navigateToAddGroupAdmins(
                    this@GroupDetailFragment,
                    viewModel.getGroup()?.id!!
                )
            }

        }
        fragment.show(childFragmentManager, null)
    }

    private fun showGroupMemberActionsDialog() {
        val fragment = GroupMemberActionsDialogFragment.newInstance()
        fragment.listener = object : GroupMemberActionsDialogFragment.Listener {
            override fun onWriteNewPost() {
                navigator.navigateToCreateTopicActivity(
                    this@GroupDetailFragment,
                    viewModel.getGroup()
                )
            }

            override fun onInvite() {
                showInviteGroupDialog()
            }

            override fun onViewMembers() {
            }
        }
        fragment.show(childFragmentManager, null)
    }

    private fun showInviteGroupDialog() {
        val dialog = InviteGroupDialogFragment.newInstance(viewModel.getGroup()!!)
        dialog.show(childFragmentManager, null)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search_threedots, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.isVisible = false
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
                activity?.invalidateOptionsMenu()
                return true
            }
        })
        super.onPrepareOptionsMenu(menu)
    }

    override fun onInviteContact() {
        navigator.navigateToPhoneContactsActivity(
            activity!!,
            InvitePhoneContactsActivity.getGroupInviteBundle(groupId)
        )
    }

    override fun onInviteDivercity() {
        navigator.navigateToInviteUsers(
            activity!!,
            InviteUsersActivity.getGroupInviteBundle(groupId)
        )
    }
}