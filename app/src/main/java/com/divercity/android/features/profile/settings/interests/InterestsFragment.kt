package com.divercity.android.features.profile.settings.interests

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.onboarding.selectinterests.SelectInterestsAdapter
import kotlinx.android.synthetic.main.fragment_interests.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class InterestsFragment : BaseFragment() {

    private lateinit var viewModel: InterestsViewModel

    @Inject
    lateinit var adapter: SelectInterestsAdapter

    companion object {

        fun newInstance(): InterestsFragment {
            return InterestsFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_interests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[InterestsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as InterestsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.interests)
            }
        }
    }

    private fun setupView(){
        list.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapter.setListener { }

        list.adapter = adapter

        btn_save.setOnClickListener {
            viewModel.followInterests(adapter.idsSelected)
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchInterestsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
                Status.SUCCESS -> {
                    adapter.list = response.data
                }
            }
        })

        viewModel.followInterestsResponse.observe(viewLifecycleOwner, Observer { response ->
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
}