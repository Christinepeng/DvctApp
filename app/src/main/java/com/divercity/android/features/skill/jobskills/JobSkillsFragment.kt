package com.divercity.android.features.skill.jobskills


import android.app.Activity
import android.content.Intent
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
import com.divercity.android.core.ui.IOnBackPressed
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.skill.jobskills.adapter.JobSkillsAdapter
import kotlinx.android.synthetic.main.fragment_job_skills.*
import kotlinx.android.synthetic.main.view_search.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class JobSkillsFragment : BaseFragment(), RetryCallback, IOnBackPressed {

    lateinit var viewModel: JobSkillsViewModel

    @Inject
    lateinit var adapter: JobSkillsAdapter

    private var handlerSearch = Handler()

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
        setupAdapter()
        subscribeToPaginatedLiveData()
        setupToolbar()
        setupSearch()
        subscribeToLiveData()
    }

    private fun setupAdapter() {
        adapter.setRetryCallback(this)
        adapter.skillsSelected =
            arguments?.getParcelableArrayList(PARAM_SKILLS) ?: ArrayList()
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

    private fun subscribeToLiveData() {

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
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

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedList().observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })

        viewModel.refreshState().observe(viewLifecycleOwner, Observer { networkState ->
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

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            viewModel.fetchData(viewLifecycleOwner, query)
        }, AppConstants.SEARCH_DELAY)
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onBackPressed(): Boolean {
        hideKeyboard()
        val intent = Intent()
        intent.putExtra(SKILLS_TITLE, adapter.skillsSelected)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
        }
        return false
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }
}
