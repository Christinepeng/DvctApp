package com.divercity.android.features.onboarding.uploadresume

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.IOnBackPressed
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.features.jobs.jobposting.skills.adapter.SkillsAdapter
import kotlinx.android.synthetic.main.fragment_job_skills.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class UploadResumeFragment : BaseFragment(), RetryCallback, IOnBackPressed {

    lateinit var viewModel: UploadResumeViewModel

    @Inject
    lateinit var adapter: SkillsAdapter

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): UploadResumeFragment {
            val fragment = UploadResumeFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_upload_resume

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[UploadResumeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.fetchSkills(null)
//        setupAdapter()
//        subscribeToPaginatedLiveData()
//        setupToolbar()
//        setupSearch()
    }

    private fun setupAdapter() {
//        adapter.setRetryCallback(this)
//        adapter.skillsPreviousSelected = arguments?.getParcelableArrayList(PARAM_SKILLS) ?: ArrayList()
//        list.adapter = adapter
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_skills)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupSearch() {
//        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
//            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                val toSearch: String? = include_search.edtxt_search.text.toString()
//                viewModel.fetchSkills(if (toSearch == "") null else toSearch)
//                subscribeToPaginatedLiveData()
//                true
//            } else
//                false
//        }
    }

    private fun subscribeToPaginatedLiveData() {
//        viewModel.pagedSkillsList.observe(this, Observer {
//            adapter.submitList(it)
//        })
//
//        viewModel.networkState().observe(this, Observer {
//            adapter.setNetworkState(it)
//        })
//
//        viewModel.refreshState().observe(this, Observer { networkState ->
//            networkState?.let {
//                adapter.currentList?.let { list ->
//                    if (networkState.status == Status.SUCCESS && list.size == 0)
//                        txt_no_results.visibility = View.VISIBLE
//                    else
//                        txt_no_results.visibility = View.GONE
//                }
//            }
//        })
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onBackPressed(): Boolean {
//        hideKeyboard()
//        val intent = Intent()
//        intent.putExtra(SKILLS_TITLE, adapter.getMergeListSelected())
//        activity?.apply {
//            setResult(Activity.RESULT_OK, intent)
//        }
        return false
    }
}
