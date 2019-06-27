package com.divercity.android.features.invitations.users

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.invitations.contacts.InvitePhoneContactsActivity.Companion.PARAM_INVITATION_TYPE
import com.divercity.android.features.invitations.users.adapter.UserSelectionAdapter
import kotlinx.android.synthetic.main.fragment_phone_contacts.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject


/**
 * Created by lucas on 24/12/2018.
 */

class InviteUsersFragment : BaseFragment(), RetryCallback {

    @Inject
    lateinit var adapter : UserSelectionAdapter

    lateinit var viewModel: InviteUsersViewModel

    private var handlerSearch = Handler()

    companion object {

        fun newInstance(data: Bundle?): InviteUsersFragment {
            val fragment = InviteUsersFragment()
            fragment.arguments = data
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_phone_contacts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)
                .get(InviteUsersViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.title = getString(R.string.invitations)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        initAdapter()
        subscribeToLiveData()
        subscribeToPaginatedLiveData()

        btn_invite.setOnClickListener {
            when(arguments?.get(PARAM_INVITATION_TYPE)){
                InviteUsersActivity.TYPE_GROUP_INVITE -> {
                    val phoneNumbers = adapter.getSelectedUsersIds()
                    if (phoneNumbers.isNotEmpty()) {
                        viewModel.inviteToGroup(
                                arguments?.getString(InviteUsersActivity.PARAM_GROUP_ID)!!,
                                phoneNumbers
                        )
                    } else {
                        showToast("Please select at least one contact")
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        adapter.setRetryCallback(this)
        list_contacts.adapter = adapter
    }

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

        viewModel.inviteUsersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast(response.data)
                    requireActivity().finish()
                }
            }
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
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

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                viewModel.fetchData(viewLifecycleOwner, query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    viewModel.fetchData(viewLifecycleOwner, newText)
                }, AppConstants.SEARCH_DELAY)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handlerSearch.removeCallbacksAndMessages(null)
    }
}