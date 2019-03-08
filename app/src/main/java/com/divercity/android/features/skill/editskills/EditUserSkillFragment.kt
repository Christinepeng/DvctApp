package com.divercity.android.features.skill.editskills

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.skill.base.SelectSkillFragment
import kotlinx.android.synthetic.main.fragment_toolbar_skills.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class EditUserSkillFragment : BaseFragment(), SelectSkillFragment.Listener {

    lateinit var viewModel: EditUserSkillViewModel

    override fun layoutId(): Int = R.layout.fragment_toolbar_skills

    lateinit var selectSkillFragment: SelectSkillFragment

    companion object {
        private const val PARAM_PREV_SKILL = "paramPrevSkills"

        fun newInstance(prevSkills: ArrayList<String>?): EditUserSkillFragment {
            val fragment = EditUserSkillFragment()
            val arguments = Bundle()
            arguments.putStringArrayList(PARAM_PREV_SKILL, prevSkills)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[EditUserSkillViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectSkillFragment = SelectSkillFragment.newInstance(arguments?.getStringArrayList(PARAM_PREV_SKILL))
        childFragmentManager.beginTransaction().add(
            R.id.fragment_fragment_container, selectSkillFragment
        ).commit()

        (activity as EditUserSkillActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_skills)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        btn_save.setOnClickListener {
            viewModel.addSkills(selectSkillFragment.getSelectedSkills())
        }

        subscribeToLiveData()
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
                    activity!!.finish()
                }
            }
        })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onSkillAddedRemoved(skills: List<String>) {

    }
}