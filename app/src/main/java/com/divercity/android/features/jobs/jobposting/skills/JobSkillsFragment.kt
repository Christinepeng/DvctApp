package com.divercity.android.features.jobs.jobposting.skills


import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.IOnBackPressed
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.jobs.jobposting.skills.adapter.SkillsAdapter
import kotlinx.android.synthetic.main.fragment_job_skills.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class JobSkillsFragment : BaseFragment(), RetryCallback, IOnBackPressed {

    lateinit var viewModel: JobSkillsViewModel

    @Inject
    lateinit var adapter: SkillsAdapter


    companion object {

        const val SKILLS_TITLE = "skillsTitle"

        private const val PARAM_SKILLS = "paramSkills"

        fun newInstance(skillsList: ArrayList<SkillResponse>): JobSkillsFragment {
            val fragment = JobSkillsFragment()
            val arguments = Bundle()
            arguments.putParcelableArrayList(PARAM_SKILLS, skillsList)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_skills

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[JobSkillsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchSkills(null)
        setupAdapter()
        subscribeToPaginatedLiveData()
        setupToolbar()
        setupSearch()
    }

    private fun setupAdapter() {
        adapter.setRetryCallback(this)
        adapter.skillsPreviousSelected = arguments?.getParcelableArrayList(PARAM_SKILLS) ?: ArrayList()
        list.adapter = adapter
    }

    private fun setupToolbar() {
        (activity as JobSkillsActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_skills)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupSearch() {
        include_search.edtxt_search.setOnKeyListener { _, keyCode, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val toSearch: String? = include_search.edtxt_search.text.toString()
                viewModel.fetchSkills(if (toSearch == "") null else toSearch)
                subscribeToPaginatedLiveData()
                true
            } else
                false
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedSkillsList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(this, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(this, Observer { networkState ->
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
        viewModel.retry()
    }

    override fun onBackPressed(): Boolean {
        hideKeyboard()
        val intent = Intent()
        intent.putExtra(SKILLS_TITLE, adapter.getMergeListSelected())
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
        }
        return false
    }
}
