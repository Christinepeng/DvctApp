package com.divercity.android.features.dialogs.jobapply

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseDialogFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.entity.job.response.JobResponse
import com.divercity.android.features.dialogs.jobapplysuccess.JobApplySuccessDialogFragment
import com.divercity.android.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.android.repository.session.SessionRepository
import kotlinx.android.synthetic.main.dialog_job_apply.view.*
import javax.inject.Inject


/**
 * Created by lucas on 18/03/2018.
 */

class JobApplyDialogFragment : BaseDialogFragment(), RecentDocsDialogFragment.Listener {

    var listener: Listener? = null

    @Inject
    lateinit var sessionRepository: SessionRepository
    lateinit var viewModel: JobApplyDialogViewModel

    private lateinit var dialogView: View

    private var docId: String? = null
    private var jobId: String? = null

    private var job: JobResponse? = null

    companion object {
        private const val JOB_ID = "jobId"
        const val REQUEST_CODE_DOC = 150
        const val DOC_ID = "docId"

        fun newInstance(jobId: String): JobApplyDialogFragment {
            val fragment = JobApplyDialogFragment()
            val arguments = Bundle()
            arguments.putString(JOB_ID, jobId)
            fragment.arguments = arguments
            return fragment
        }

        fun newInstance(job: JobResponse): JobApplyDialogFragment {
            DataHolder.data = job
            return JobApplyDialogFragment()
        }
    }

    enum class DataHolder {
        INSTANCE;

        private var job: JobResponse? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.job != null
            }

            var data: JobResponse?
                get() {
                    val jobResponse = INSTANCE.job
                    INSTANCE.job = null
                    return jobResponse
                }
                set(objectList) {
                    INSTANCE.job = objectList
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = DataHolder.data
        if (job != null)
            jobId = job!!.id
        else
            jobId = arguments?.getString(JOB_ID)
        savedInstanceState?.also {
            docId = it.getString(DOC_ID)
            jobId = it.getString(JOB_ID)
        }
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[JobApplyDialogViewModel::class.java]
        subscribeToLiveData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_job_apply, null)

        GlideApp.with(this)
            .load(sessionRepository.getUserAvatarUrl())
            .apply(RequestOptions().circleCrop())
            .into(dialogView.img_profile)

        dialogView.apply {
            txt_fullname.text = sessionRepository.getUserName()

            txt_usr_occupation.visibility = View.GONE
            txt_usr_location.visibility = View.GONE

            btn_upload_new.setOnClickListener {
                openDocSelector()
            }

            btn_choose_recent_file.setOnClickListener {
                showRecentDocsDialog()
            }

            btn_submit_application.setOnClickListener {
                if (docId != null && jobId != null) {
                    viewModel.applyToJob(jobId!!, docId!!, et_cover_letter.text.toString())
                } else
                    showToast(R.string.select_valid_file)
            }

            btn_deselect_doc.setOnClickListener {
                docId = null
                lay_doc_detail.visibility = View.GONE
                btn_choose_recent_file.visibility = View.VISIBLE
            }

            btn_close.setOnClickListener { dismiss() }

            builder.setView(this)
        }
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK) {
            handleDocSelectorActivityResult(data)
        }
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
                    showDocData(document.data)
                }
            }
        })

        viewModel.applyToJobResponse.observe(this, Observer { document ->
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
                    job?.attributes?.isAppliedByCurrent = true
                    listener?.onSuccessJobApply()
                    showJobApplySuccessDialog()
                    dismiss()
                }
            }
        })
    }

    private fun showJobApplySuccessDialog() {
        val fragment = JobApplySuccessDialogFragment.newInstance(jobId!!)
        fragment.show(requireActivity().supportFragmentManager, null)
    }

    private fun showDocData(doc: DocumentResponse?) {
        docId = doc?.id
        dialogView.also {
            it.btn_choose_recent_file.visibility = View.GONE
            it.lay_doc_detail.visibility = View.VISIBLE
            it.txt_filename.text = doc?.attributes?.name
            it.txt_created_at.text = context?.getString(
                R.string.created_at,
                Util.getStringDateTimeWithServerDate(doc?.attributes?.createdAt)
            )
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
        startActivityForResult(intent, REQUEST_CODE_DOC)
    }

    private fun handleDocSelectorActivityResult(data: Intent?) {
        val fileUri = data?.data
        if (fileUri != null) {
            viewModel.checkDocumentAndUploadIt(fileUri)
        } else
            showToast(R.string.select_valid_file)
    }

    private fun showToast(msg: String) {
        Toast.makeText(context!!, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(DOC_ID, docId)
        outState.putString(JOB_ID, jobId)
        super.onSaveInstanceState(outState)
    }

    private fun showRecentDocsDialog() {
        val dialog = RecentDocsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    private fun showProgress() = progressStatus(View.VISIBLE)

    private fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        dialogView.include_loading.visibility = viewStatus
    }

    override fun onDocumentClick(doc: DocumentResponse) {
        showDocData(doc)
    }

    interface Listener {

        fun onSuccessJobApply()
    }
}