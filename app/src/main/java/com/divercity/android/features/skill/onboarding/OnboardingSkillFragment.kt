package com.divercity.android.features.skill.onboarding


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.company.selectcompany.base.adapter.CompanyAdapter
import com.divercity.android.features.skill.base.SelectSkillFragment
import kotlinx.android.synthetic.main.fragment_onboarding_header_search_list.*
import kotlinx.android.synthetic.main.view_header_profile.view.*
import javax.inject.Inject

class OnboardingSkillFragment : BaseFragment(), SelectSkillFragment.Listener {

    lateinit var viewModel: OnboardingSkillViewModel

    @Inject
    lateinit var adapter: CompanyAdapter

    var currentProgress: Int = 0

    lateinit var selectSkillFragment: SelectSkillFragment

    companion object {
        private const val PARAM_PROGRESS = "paramProgress"

        fun newInstance(progress: Int): OnboardingSkillFragment {
            val fragment = OnboardingSkillFragment()
            val arguments = Bundle()
            arguments.putInt(PARAM_PROGRESS, progress)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_toolbar_onboarding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[OnboardingSkillViewModel::class.java]
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectSkillFragment = SelectSkillFragment.newInstance(null)
        childFragmentManager.beginTransaction().add(
            R.id.fragment_fragment_container, selectSkillFragment
        ).commit()
        setupHeader()
        subscribeToLiveData()
    }

    private fun setupHeader() {

        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.select_your_skills)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(requireActivity())
            }

            btn_skip.setOnClickListener {
                if (include_header.btn_skip.text == getText(R.string.skip))
                    navigator.navigateToNextOnboarding(
                        requireActivity(),
                        viewModel.accountType,
                        currentProgress,
                        false
                    )
                else {
                    viewModel.addSkills(selectSkillFragment.getSelectedSkills())
                }
            }
        }
    }

    fun checkDoneSkip(skills: List<String>) {
        if (skills.isNotEmpty()) {
            include_header.btn_skip.text = getText(R.string.next)
        } else {
            include_header.btn_skip.text = getText(R.string.skip)
        }
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
                        requireActivity(),
                        viewModel.accountType,
                        currentProgress,
                        true
                    )
                }
            }
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSkillAddedRemoved(skills: List<String>) {
        checkDoneSkip(skills)
    }
}
