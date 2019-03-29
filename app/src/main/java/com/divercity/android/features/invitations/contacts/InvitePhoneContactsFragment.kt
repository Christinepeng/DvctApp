package com.divercity.android.features.invitations.contacts

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
import com.divercity.android.core.contactfetcher.Contact
import com.divercity.android.core.contactfetcher.ContactFetcher
import com.divercity.android.core.contactfetcher.ContactListener
import com.divercity.android.data.Status
import com.divercity.android.features.invitations.contacts.InvitePhoneContactsActivity.Companion.PARAM_INVITATION_TYPE
import com.divercity.android.features.invitations.contacts.adapter.PhoneContactAdapter
import com.divercity.android.features.invitations.contacts.model.PhoneContact
import kotlinx.android.synthetic.main.fragment_phone_contacts.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject


/**
 * Created by lucas on 24/12/2018.
 */

class InvitePhoneContactsFragment : BaseFragment() {

    @Inject
    lateinit var adapter : PhoneContactAdapter

    lateinit var viewModel: InvitePhoneContactsViewModel

    private var handlerSearch = Handler()

    companion object {

        fun newInstance(data: Bundle?): InvitePhoneContactsFragment {
            val fragment = InvitePhoneContactsFragment()
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
                .get(InvitePhoneContactsViewModel::class.java)
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

        setupView()
        showContacts()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.inviteContactsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast(response.data)
                    activity!!.finish()
                }
            }
        })

    }

    private fun setupView() {
        list_contacts.adapter = adapter

        btn_invite.setOnClickListener {
            when(arguments?.get(PARAM_INVITATION_TYPE)){
                InvitePhoneContactsActivity.TYPE_GROUP_INVITE -> {
                    val phoneNumbers = adapter.getSelectedPhoneNumbers()
                    if (phoneNumbers.isNotEmpty()) {
                        viewModel.inviteToGroup(
                            arguments?.getString(InvitePhoneContactsActivity.PARAM_GROUP_ID)!!,
                            adapter.getSelectedPhoneNumbers()
                        )
                    } else {
                        showToast("Please select at least one contact")
                    }
                }
            }
        }
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        ContactFetcher.resolvePermissionResult(this, requestCode, permissions, grantResults);
    }

    private fun showContacts() {
        showProgress()
        val contactLists = ArrayList<PhoneContact>()
        ContactFetcher.getContacts(this@InvitePhoneContactsFragment, object :
            ContactListener<Contact> {
            override fun onNext(contact: Contact) {
                if (contact.phoneNumbers.isNotEmpty()) {
                    contact.phoneNumbers.forEach {
                        val phoneContact =
                            PhoneContact(contact.displayName, it)
                        contactLists.add(phoneContact)
                    }
                }
            }

            override fun onError(error: Throwable) {
                hideProgress()
                Toast.makeText(context,error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onComplete() {
                hideProgress()
                adapter.setListData(contactLists)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                handlerSearch.removeCallbacksAndMessages(null)
                handlerSearch.postDelayed({
                    adapter.search(newText)
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