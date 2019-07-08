package com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilterlocation

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.dialogs.jobsearchfilter.JobSearchFilterDialogViewModel
import com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilterlocation.adapter.LocationMultipleSelectionAdapter
import com.divercity.android.features.location.base.SelectLocationViewModel
import kotlinx.android.synthetic.main.fragment_job_search_filter_location.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterLocationFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: JobSearchFilterDialogViewModel

    @Inject
    lateinit var adapter: LocationMultipleSelectionAdapter

    private lateinit var viewModelLocation: SelectLocationViewModel

    private var listener: IJobSearchFilter? = null

    private var handlerSearch = Handler()

    companion object {

        fun newInstance(): JobSearchFilterLocationFragment {
            return JobSearchFilterLocationFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter_location

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // To know if the dialog is being called from an activity or fragment
            parentFragment?.let {
                listener = it as IJobSearchFilter
            }
            if (listener == null)
                listener = context as IJobSearchFilter
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling context must implement JobPostedDialogFragment.Listener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
            requireActivity(),
            viewModelFactory
        )[JobSearchFilterDialogViewModel::class.java]
        viewModelLocation = ViewModelProviders.of(this, viewModelFactory)
            .get(SelectLocationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initList()
        setupSearch()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun initView() {
        btn_select_all.isSelected = viewModel.isAllLocationSelected
        btn_back.setOnClickListener {
            if (btn_select_all.isSelected)
                viewModel.locationFilter.value = "All"
            else
                viewModel.locationFilter.value = adapter.getSelectedLocationsString()
            viewModel.isAllLocationSelected = btn_select_all.isSelected
            viewModel.selectedLocations = adapter.selectedLocations
            listener?.onBackPressed()
        }

        lay_all.setOnClickListener {
            btn_select_all.isSelected = !btn_select_all.isSelected
            if (btn_select_all.isSelected) {
                adapter.onSelectAll()
            }
        }
    }

    private fun initList() {
        adapter.selectedLocations = viewModel.selectedLocations
        adapter.setRetryCallback(this)
        adapter.onSelectUnselectLocation = {
            btn_select_all.isSelected = false
        }
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModelLocation.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })

        viewModel.locationFilter.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModelLocation.pagedList().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModelLocation.networkState().observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        viewModelLocation.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
            networkState?.let {
                adapter.currentList?.let { list ->
                    if (networkState.status == Status.SUCCESS && list.size == 0)
                        txt_no_results.visibility = View.VISIBLE
                    else
                        txt_no_results.visibility = View.GONE
                }
            }
        })
    }

    private fun setupSearch() {

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

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModelLocation.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    override fun retry() {
        viewModelLocation.retry()
    }
}