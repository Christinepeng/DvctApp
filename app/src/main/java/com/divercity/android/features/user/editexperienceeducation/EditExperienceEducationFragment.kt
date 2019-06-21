package com.divercity.android.features.user.editexperienceeducation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.ui.IOnBackPressed
import com.divercity.android.data.Status
import com.divercity.android.features.education.addediteducation.adapter.EducationAdapter
import com.divercity.android.features.user.addeditworkexperience.adapter.WorkExperienceAdapter
import kotlinx.android.synthetic.main.fragment_edit_experience_education.*
import kotlinx.android.synthetic.main.view_network_state.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

class EditExperienceEducationFragment : BaseFragment(), IOnBackPressed {

    @Inject
    lateinit var workExperienceAdapter: WorkExperienceAdapter

    @Inject
    lateinit var educationAdapter: EducationAdapter

    private lateinit var viewModel: EditExperienceEducationViewModel

    private var mustUpdate = false

    companion object {

        const val REQUEST_CODE_EDUCATION = 180
        const val REQUEST_CODE_EXPERIENCE = 300

        fun newInstance(): EditExperienceEducationFragment {
            return EditExperienceEducationFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_edit_experience_education

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(
                this,
                viewModelFactory
            )[EditExperienceEducationViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as EditExperienceEducationActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setDisplayHomeAsUpEnabled(true)
                it.setTitle(R.string.experience_education)
            }
        }
    }

    private fun setupView() {
        workExperienceAdapter.onWorkExperienceSelect = {
            navigator.navigateToEditWorkExperienceForResult(this, it, REQUEST_CODE_EXPERIENCE)
        }
        workExperienceAdapter.onWorkExperienceDelete = {
            viewModel.deleteWorkExperience(it.id)
        }
        workExperienceAdapter.isEdition = true
        list_experience.adapter = workExperienceAdapter


        educationAdapter.onEducationSelect = {
            navigator.navigateToEditEducationForResult(this, it, REQUEST_CODE_EDUCATION)
        }
        educationAdapter.onEducationDelete = {
            viewModel.deleteEducation(it.id)
        }
        educationAdapter.isEdition = true
        list_education.adapter = educationAdapter


        btn_add_work_experience.setOnClickListener {
            navigator.navigateToAddWorkExperienceForResult(
                this,
                REQUEST_CODE_EXPERIENCE
            )
        }

        btn_add_education.setOnClickListener {
            navigator.navigateToAddEducationForResult(
                this,
                REQUEST_CODE_EDUCATION
            )
        }

        view_network_state_education.apply {
            view_net_sta_btn_retry.visibility = View.GONE
            view_net_sta_progress.visibility = View.GONE
            view_net_sta_txt_msg.visibility = View.GONE

            view_net_sta_btn_retry.setOnClickListener {
                viewModel.fetchEducations()
            }
        }

        view_network_state_experience.apply {
            view_net_sta_btn_retry.visibility = View.GONE
            view_net_sta_progress.visibility = View.GONE
            view_net_sta_txt_msg.visibility = View.GONE

            view_net_sta_btn_retry.setOnClickListener {
                viewModel.fetchWorkExperiences()
            }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchWorkExperiencesResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showLoadingExperience(true)
                }
                Status.ERROR -> {
                    showErrorExperience(true, response.message)
                    list_experience.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    showLoadingExperience(false)
                    list_experience.visibility = View.VISIBLE
                    workExperienceAdapter.list = response.data!!
                }
            }
        })

        viewModel.fetchEducationsResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showLoadingEducation(true)
                }
                Status.ERROR -> {
                    showErrorEducation(true, response.message)
                    list_education.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    showLoadingEducation(false)
                    list_education.visibility = View.VISIBLE
                    educationAdapter.list = response.data!!
                }
            }
        })

        viewModel.deleteEducationResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    mustUpdate = true
                    viewModel.fetchEducations()
                }
            }
        })

        viewModel.deleteWorkExperienceResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    mustUpdate = true
                    viewModel.fetchWorkExperiences()
                }
            }
        })
    }

    private fun showLoadingExperience(show: Boolean) {
        view_network_state_experience.apply {
            if (show) {
                view_net_sta_btn_retry.visibility = View.GONE
                view_net_sta_progress.visibility = View.VISIBLE
                view_net_sta_txt_msg.visibility = View.GONE
            } else {
                view_net_sta_btn_retry.visibility = View.GONE
                view_net_sta_progress.visibility = View.GONE
                view_net_sta_txt_msg.visibility = View.GONE
            }
        }
    }

    private fun showErrorExperience(show: Boolean, msg: String?) {
        view_network_state_experience.apply {
            if (show) {
                view_net_sta_btn_retry.visibility = View.VISIBLE
                view_net_sta_progress.visibility = View.GONE
                view_net_sta_txt_msg.visibility = View.VISIBLE
                view_net_sta_txt_msg.text = msg
            } else {
                view_net_sta_btn_retry.visibility = View.GONE
                view_net_sta_progress.visibility = View.GONE
                view_net_sta_txt_msg.visibility = View.GONE
            }
        }
    }

    private fun showLoadingEducation(show: Boolean) {
        view_network_state_education.apply {
            if (show) {
                view_net_sta_btn_retry.visibility = View.GONE
                view_net_sta_progress.visibility = View.VISIBLE
                view_net_sta_txt_msg.visibility = View.GONE
            } else {
                view_net_sta_btn_retry.visibility = View.GONE
                view_net_sta_progress.visibility = View.GONE
                view_net_sta_txt_msg.visibility = View.GONE
            }
        }
    }

    private fun showErrorEducation(show: Boolean, msg: String?) {
        view_network_state_education.apply {
            if (show) {
                view_net_sta_btn_retry.visibility = View.VISIBLE
                view_net_sta_progress.visibility = View.GONE
                view_net_sta_txt_msg.visibility = View.VISIBLE
                view_net_sta_txt_msg.text = msg
            } else {
                view_net_sta_btn_retry.visibility = View.GONE
                view_net_sta_progress.visibility = View.GONE
                view_net_sta_txt_msg.visibility = View.GONE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                REQUEST_CODE_EDUCATION -> {
                    mustUpdate = true
                    viewModel.fetchEducations()
                }
                REQUEST_CODE_EXPERIENCE -> {
                    mustUpdate = true
                    viewModel.fetchWorkExperiences()
                }
            }
    }

    override fun onBackPressed(): Boolean {
        if (mustUpdate)
            activity?.apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        return true
    }
}