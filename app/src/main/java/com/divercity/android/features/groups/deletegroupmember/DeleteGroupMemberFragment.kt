package com.divercity.android.features.groups.deletegroupmember

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.user.useradapter.groupadmin.GroupAdminAdapter
import kotlinx.android.synthetic.main.fragment_delete_group_admin.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class DeleteGroupMemberFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: DeleteGroupMemberViewModel

    @Inject
    lateinit var adapter: GroupAdminAdapter

    private var handlerSearch = Handler()

    companion object {

        private const val PARAM_GROUP_ID = "paramGroupId"

        fun newInstance(groupId: String): DeleteGroupMemberFragment {
            val fragment = DeleteGroupMemberFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_GROUP_ID, groupId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_delete_group_admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DeleteGroupMemberViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.groupId = arguments?.getString(PARAM_GROUP_ID)!!
        viewModel.fetchData()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.edit_members)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        viewModel.fetchData(null, "")

        initView()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()
    }

    private fun initView() {
        initAdapter()

        include_search.visibility = View.GONE

        btn_action.setOnClickListener {
            val selected = adapter.getSelectedUsersIds()
            if (selected.isNotEmpty()) {
                viewModel.deleteGroupMember(
                    adapter.getSelectedUsersIds()
                )
            } else {
                showToast("Select at least one")
            }
        }

        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                val toSearch: String? = include_search.edtxt_search.text.toString()

                search(toSearch)
                true
            } else
                false
        }

        include_search.edtxt_search.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                search(p0.toString())
            }
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initAdapter() {
        adapter.ownerId = ""
        adapter.setRetryCallback(this)
        list.adapter = adapter
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

        viewModel.deleteGroupMemberResponse.observe(viewLifecycleOwner, Observer { response ->
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
                    Toast.makeText(activity, response.data, Toast.LENGTH_SHORT).show()
                    viewModel.refresh()
                }
            }
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            adapter.currentList?.let { pagedList ->
                if (networkState?.status == Status.SUCCESS && pagedList.size == 0)
                    txt_no_results.visibility = View.VISIBLE
                else
                    txt_no_results.visibility = View.GONE
            }
        })
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}