package com.divercity.android.features.ethnicity.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.model.Ethnicity
import kotlinx.android.synthetic.main.fragment_select_ethnicity.*
import kotlinx.android.synthetic.main.view_retry.view.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */

class SelectEthnicityFragment : BaseFragment() {

    lateinit var viewModel: SelectEthnicityViewModel

    @Inject
    lateinit var adapter: EthnicityAdapter

    var fragListener: Listener? = null

    companion object {

        fun newInstance(): SelectEthnicityFragment {
            return SelectEthnicityFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_select_ethnicity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SelectEthnicityViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragListener = parentFragment as Listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        include_retry.btn_retry.setOnClickListener {
            viewModel.fetchEthnicies()
        }

        adapter.onEthnicitySelected = {
            fragListener?.onEthnicityChosen(it)
        }
        list.adapter = adapter

        subscribeToLiveData()
    }

    interface Listener {

        fun onEthnicityChosen(ethnicity: Ethnicity)
    }

    private fun subscribeToLiveData() {
        viewModel.fetchEthniciesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showRetry(false)
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showRetry(true)
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    showRetry(false)
                    hideProgress()
                    adapter.list = response.data!!
                }
            }
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showRetry(show: Boolean){
        if(show)
            include_retry.visibility = View.VISIBLE
        else
            include_retry.visibility = View.GONE
    }
}