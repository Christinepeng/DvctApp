package com.divercity.android.features.jobs.jobs.search.searchfilterlocation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.jobsearchfilter.IJobSearchFilter
import com.divercity.android.features.jobs.jobs.search.searchfilterlocation.adapter.LocationMultipleSelectionAdapter
import com.divercity.android.features.location.base.SelectLocationViewModel
import kotlinx.android.synthetic.main.fragment_job_search_filter_location.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class JobSearchFilterLocationFragment : BaseFragment(), RetryCallback {

    @Inject
    lateinit var adapter: LocationMultipleSelectionAdapter

    lateinit var viewModelLocation: SelectLocationViewModel

    private var listener: IJobSearchFilter? = null

    companion object {

        fun newInstance(): JobSearchFilterLocationFragment {
            return JobSearchFilterLocationFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_search_filter_location

    override fun onAttach(context: Context?) {
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
        viewModelLocation = ViewModelProviders.of(this, viewModelFactory)
            .get(SelectLocationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initList()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun initView(){
        btn_back.setOnClickListener {
            listener?.onBackPressed()
        }
    }

    private fun initList() {
        adapter.setRetryCallback(this)
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModelLocation.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun subscribeToPaginatedLiveData() {
        viewModelLocation.pagedList.observe(viewLifecycleOwner, Observer {
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

    override fun retry() {
        viewModelLocation.retry()
    }
}