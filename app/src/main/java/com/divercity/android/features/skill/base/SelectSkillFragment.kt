package com.divercity.android.features.skill.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import co.lujun.androidtagview.TagView
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.RetryCallback
import com.divercity.android.data.Status
import com.divercity.android.data.entity.skills.SkillResponse
import com.divercity.android.features.skill.base.adapter.SkillsOnboardingAdapter
import com.divercity.android.features.skill.base.adapter.SkillsOnboardingViewHolder
import kotlinx.android.synthetic.main.fragment_base_skill.*
import kotlinx.android.synthetic.main.view_search.view.*
import javax.inject.Inject

class SelectSkillFragment : BaseFragment(), RetryCallback {

    lateinit var viewModel: SelectSkillViewModel

    @Inject
    lateinit var adapter: SkillsOnboardingAdapter

    var currentProgress: Int = 0
    private var handlerSearch = Handler()

    private var fragmentListener: Listener? = null

    companion object {

        private const val PARAM_PREV_SKILLS = "paramPrevSkills"

        fun newInstance(previousSkills: ArrayList<String>?): SelectSkillFragment {
            val fragment = SelectSkillFragment()
            val arguments = Bundle()
            arguments.putStringArrayList(PARAM_PREV_SKILLS, previousSkills)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_base_skill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SelectSkillViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupAdapter()
        setupView()
        subscribeToPaginatedLiveData()
        subscribeToLiveData()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        fragmentListener = parentFragment as Listener
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
    }


    private fun setupAdapter() {
        adapter.setRetryCallback(this)
        adapter.listener = listener
        list.adapter = adapter
    }

    private fun setupView() {
        val prevSkills = arguments?.getStringArrayList(PARAM_PREV_SKILLS)
        if (prevSkills != null) tagview_skills.tags = prevSkills

        btn_add_skill.setOnClickListener {
            val skill = edtxt_skill.text.toString()
            if (skill.isNotEmpty()) {
                tagview_skills.addTag(skill)
                edtxt_skill.text.clear()
                fragmentListener?.onSkillAddedRemoved(tagview_skills.tags)
            }
        }

        tagview_skills.setOnTagClickListener(object : TagView.OnTagClickListener {

            override fun onTagClick(position: Int, text: String) {}

            override fun onTagLongClick(position: Int, text: String) {}

            override fun onSelectedTagDrag(position: Int, text: String) {}

            override fun onTagCrossClick(position: Int) {
                tagview_skills.removeTag(position)
                fragmentListener?.onSkillAddedRemoved(tagview_skills.tags)
            }
        })
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

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPaginatedLiveData()
        })
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

    fun getSelectedSkills(): List<String> {
        return tagview_skills.tags
    }

    val listener = object : SkillsOnboardingViewHolder.Listener {

        override fun onSkillSelect(skill: SkillResponse) {
            tagview_skills.addTag(skill.attributes?.name)
            fragmentListener?.onSkillAddedRemoved(tagview_skills.tags)
        }
    }

    interface Listener {

        fun onSkillAddedRemoved(skills: List<String>)
    }
}
