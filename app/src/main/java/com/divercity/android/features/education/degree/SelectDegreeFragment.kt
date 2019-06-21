package com.divercity.android.features.education.degree

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.education.degree.adapter.DegreeAdapter
import kotlinx.android.synthetic.main.fragment_select_degree.*
import kotlinx.android.synthetic.main.fragment_toolbar.include_toolbar
import kotlinx.android.synthetic.main.view_retry.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 06/11/2018.
 */

class SelectDegreeFragment : BaseFragment() {

    lateinit var viewModel: SelectDegreeViewModel

    @Inject
    lateinit var adapter: DegreeAdapter

    override fun layoutId(): Int = R.layout.fragment_select_degree

    companion object {

        const val DEGREE_PICKED = "degreePicked"

        fun newInstance(): SelectDegreeFragment {
            return SelectDegreeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SelectDegreeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as SelectDegreeActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_degree)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        setupView()
        subscribeToLiveData()
    }

    private fun setupView() {
        adapter.onDegreeSelected = {
            val intent = Intent()
            intent.putExtra(DEGREE_PICKED, it)
            requireActivity().apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        list.adapter = adapter

        include_retry.btn_retry.setOnClickListener {
            viewModel.fetchDegrees()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchDegreesResponse.observe(viewLifecycleOwner, Observer { response ->
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