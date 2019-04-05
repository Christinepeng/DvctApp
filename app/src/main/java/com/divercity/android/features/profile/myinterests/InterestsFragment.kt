package com.divercity.android.features.profile.myinterests

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
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

    private var isEdition = false

    companion object {

        private const val PARAM_IS_EDITION = "paramIsEdition"

        fun newInstance(isEdition: Boolean): InterestsFragment {
            val fragment = InterestsFragment()
            val arguments = Bundle()
            arguments.putBoolean(PARAM_IS_EDITION, isEdition)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_interests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[InterestsViewModel::class.java]
        isEdition = arguments!!.getBoolean(PARAM_IS_EDITION, false)
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

    private fun setupView() {
        list.layoutManager = StaggeredGridLayoutManager(2, 1)

        list.adapter = adapter

        if (isEdition) {
            adapter.setListener { }
            btn_save.visibility = View.VISIBLE
            btn_save.setOnClickListener {
                viewModel.followInterests(adapter.idsSelected)
            }
            adapter.setListener {}
        } else {
            adapter.setListener(null)
            btn_save.visibility = View.GONE
            list.isEnabled = false
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchInterestsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showFetchInterestsErrorDialog(response.message ?: "Error")
                }
                Status.SUCCESS -> {
                    hideProgress()
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

    private fun showFetchInterestsErrorDialog(msg: String) {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            "Ups!",
            msg,
            getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { viewModel.fetchInterests() }
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }
}