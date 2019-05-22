package com.divercity.android.features.onboarding.uploadresume

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.jobapply.JobApplyDialogFragment
import com.divercity.android.features.skill.jobskills.adapter.JobSkillsAdapter
import kotlinx.android.synthetic.main.fragment_onboarding_upload_resume.*
import kotlinx.android.synthetic.main.view_header_profile.*
import javax.inject.Inject

class UploadResumeFragment : BaseFragment() {

    lateinit var viewModel: UploadResumeViewModel

    @Inject
    lateinit var adapter: JobSkillsAdapter

    var currentProgress: Int = 0

    companion object {
        const val REQUEST_CODE_DOC = 150
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
        currentProgress = arguments?.getInt(PARAM_PROGRESS) ?: 0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupView()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.uploadDocumentResponse.observe(this, Observer { document ->
            when (document?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(document.message ?: "Error")
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showToast(R.string.file_upload_success)
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

    private fun setupView(){
        btn_upload_resume.setOnClickListener {
            openDocSelector()
        }
    }

    private fun setupHeader() {

        include_header.apply {

            progress_bar.apply {
                max = 100
                progress = 0
                setProgressWithAnim(currentProgress)
            }
            txt_title.setText(R.string.upload_your_resume)

            txt_progress.text = currentProgress.toString().plus("%")

            btn_close.setOnClickListener {
                navigator.navigateToHomeActivity(requireActivity())
            }

            btn_skip.setOnClickListener {
                navigator.navigateToNextOnboarding(
                    requireActivity(),
                    viewModel.accountType,
                    currentProgress,
                    false
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK) {
            handleDocSelectorActivityResult(data)
        }
    }

    private fun openDocSelector() {
        val mimeTypes = arrayOf(
            "application/pdf"
        )
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, JobApplyDialogFragment.REQUEST_CODE_DOC)
    }

    private fun handleDocSelectorActivityResult(data: Intent?) {
        val fileUri = data?.data
        if (fileUri != null) {
            viewModel.checkDocumentAndUploadIt(fileUri)
        } else
            showToast(R.string.select_valid_file)
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(msg: String) {
        Toast.makeText(context!!, msg, Toast.LENGTH_SHORT).show()
    }

}
