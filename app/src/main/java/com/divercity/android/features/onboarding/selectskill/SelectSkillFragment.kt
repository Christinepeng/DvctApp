package com.divercity.android.features.onboarding.selectskill


import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.lujun.androidtagview.TagView
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.onboarding.selectskill.adapter.SkillsOnboardingAdapter
import com.divercity.android.features.onboarding.selectskill.adapter.SkillsOnboardingViewHolder
import kotlinx.android.synthetic.main.fragment_onboarding_skills.*
import kotlinx.android.synthetic.main.view_header_profile.view.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectSkillFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectSkillViewModel

    @Inject
    lateinit var adapter: SkillsOnboardingAdapter

    var currentProgress: Int = 0
    private var handlerSearch = Handler()

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): SelectSkillFragment {
            val fragment = SelectSkillFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_onboarding_skills

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectSkillViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupAdapter()
        setupView()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    private fun fetchSkills(searchQuery: String?) {
        viewModel.fetchSkills(viewLifecycleOwner, searchQuery)
    }

    private fun setupHeader() {

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

        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_skills)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(activity!!)
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(
                    activity!!,
                    viewModel.accountType,
                    currentProgress,
                    false
                )
            }
        }
    }


    private fun setupAdapter() {
        adapter.setRetryCallback(this)
        adapter.listener = listener
        list.adapter = adapter
    }

    private fun setupView() {
        btn_add_skill.setOnClickListener {
            val skill = edtxt_skill.text.toString()
            if (skill.isNotEmpty()) {
                tagview_skills.addTag(skill)
                edtxt_skill.text.clear()
                checkDoneSkip()
            }
        }

        tagview_skills.setOnTagClickListener(object : TagView.OnTagClickListener {

            override fun onTagClick(position: Int, text: String) {}

            override fun onTagLongClick(position: Int, text: String) {}

            override fun onSelectedTagDrag(position: Int, text: String) {}

            override fun onTagCrossClick(position: Int) {
                tagview_skills.removeTag(position)
                checkDoneSkip()
            }
        })

        include_header.btn_skip.setOnClickListener {
            if(include_header.btn_skip.text == getText(R.string.skip))
                navigator.navigateToNextOnboarding(
                    activity!!,
                    viewModel.accountType,
                    currentProgress,
                    false
                )
            else
                viewModel.addSkills(tagview_skills.tags)

        }
    }

    fun checkDoneSkip(){
        if(tagview_skills.tags.isNotEmpty()){
            include_header.btn_skip.text = getText(R.string.next)
        } else {
            include_header.btn_skip.text = getText(R.string.skip)
        }
    }

    private fun subscribeToPaginatedLiveData() {
        viewModel.pagedSkillsList.observe(viewLifecycleOwner, Observer {
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

    private fun subscribeToLiveData() {

        viewModel.updateUserProfileResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    hideProgress()
                    navigator.navigateToNextOnboarding(
                        activity!!,
                        viewModel.accountType,
                        currentProgress,
                        true
                    )
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun search(query: String?) {
        handlerSearch.removeCallbacksAndMessages(null)
        handlerSearch.postDelayed({
            fetchSkills(if (query == "") null else query)
        }, AppConstants.SEARCH_DELAY)
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onDestroyView() {
        handlerSearch.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    val listener = object : SkillsOnboardingViewHolder.Listener {

        override fun onSkillSelect(skill: SkillResponse) {
            tagview_skills.addTag(skill.attributes?.name)
            checkDoneSkip()
        }
    }
}
