package com.divercity.app.features.dialogs.jobapplication

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.base.BaseDialogFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.core.utils.Util
import com.divercity.app.data.Status
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.data.entity.jobapplication.JobApplicationResponse
import com.divercity.app.features.dialogs.CustomTwoBtnDialogFragment
import com.divercity.app.features.dialogs.recentdocuments.RecentDocsDialogFragment
import kotlinx.android.synthetic.main.dialog_job_application.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class JobApplicationDialogFragment : BaseDialogFragment(), RecentDocsDialogFragment.Listener {

    lateinit var viewModel: JobApplicationDialogViewModel

    private lateinit var dialogView: View

    private var isEditing = false
    private var docId: String? = null
    lateinit var jobId: String

    private var listener: Listener? = null

    private var jobApplicationResponse: JobApplicationResponse? = null

    companion object {
        private const val JOB_ID = "jobId"
        const val REQUEST_CODE_DOC = 150
        const val DOC_ID = "docId"

        fun newInstance(jobId: String): JobApplicationDialogFragment {
            val fragment =
                    JobApplicationDialogFragment()
            val arguments = Bundle()
            arguments.putString(JOB_ID, jobId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            // To know if the dialog is being called from an activity or fragment
            parentFragment?.let {
                listener = it as Listener
            }
            if (listener == null)
                listener = context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling context must implement JobPostedDialogFragment.Listener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[JobApplicationDialogViewModel::class.java]

        if (savedInstanceState == null) {
            jobId = arguments?.getString(JOB_ID)!!
            viewModel.fetchJobApplication(jobId)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_application, null)

        dialogView.apply {
            subscribeToLiveData()

            lay_main.visibility = View.GONE
            txt_usr_occupation.visibility = View.GONE
            txt_usr_location.visibility = View.GONE

            btn_close.setOnClickListener {
                if (isEditing)
                    setAndShowData(jobApplicationResponse!!, false)
                else
                    dismiss()
            }

            btn_choose_recent_file.setOnClickListener {
                showRecentDocsDialog()
            }

            btn_edit_application.setOnClickListener {
                showEditionView(true)
            }

            btn_deselect_doc.setOnClickListener {
                docId = null
                lay_doc_detail.visibility = View.GONE
                btn_choose_recent_file.visibility = View.VISIBLE
            }

            btn_upload_new.setOnClickListener {
                openDocSelector()
            }

            btn_save_application.setOnClickListener {
                viewModel.editJobApplication(
                        jobId,
                        docId!!,
                        et_cover_letter.text.toString())
            }

            btn_cancel_application.setOnClickListener {
                showCancelDialogConfirm()
            }

            builder.setView(this)
        }
        return builder.create()
    }

    fun showEditionView(show: Boolean) {
        dialogView.apply {
            if (show) {
                isEditing = true
                btn_edit_application.visibility = View.GONE
                btn_cancel_application.visibility = View.GONE

                btn_deselect_doc.visibility = View.VISIBLE
                btn_upload_new.visibility = View.VISIBLE
                btn_save_application.visibility = View.VISIBLE

                et_cover_letter.isEnabled = true

            } else {
                isEditing = false
                btn_edit_application.visibility = View.VISIBLE
                btn_cancel_application.visibility = View.VISIBLE

                btn_choose_recent_file.visibility = View.GONE
                lay_doc_detail.visibility = View.VISIBLE
                btn_deselect_doc.visibility = View.GONE
                btn_upload_new.visibility = View.GONE
                btn_save_application.visibility = View.GONE

                et_cover_letter.isEnabled = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    private fun subscribeToLiveData() {
        viewModel.fetchJobApplicationResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    showToast(response.message ?: "Error")
                    dismiss()
                }
                Status.SUCCESS -> {
                    dialogView.lay_main.visibility = View.VISIBLE
                    setAndShowData(response.data!!,false)
                }
            }
        })

        viewModel.cancelJobApplicationResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message ?: "Error")
                }

                Status.SUCCESS -> {
                    hideProgress()
                    listener?.onCancelJobApplication()
                    dismiss()
                }
            }
        })

        viewModel.editJobApplicationResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(response.message ?: "Error")
                    dismiss()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    setAndShowData(response.data!!, false)
                }
            }
        })

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
    }

    private fun setAndShowData(application: JobApplicationResponse, isEdition : Boolean){
        jobApplicationResponse = application

        docId = application.attributes?.userDocumentId!!.toString()

        showEditionView(isEdition)

        dialogView.apply {

            GlideApp.with(this)
                    .load(application.attributes.applicant?.photos?.medium)
                    .apply(RequestOptions().circleCrop())
                    .into(img_profile)

            txt_fullname.text = application.attributes.applicant?.name

            txt_filename.text = application.attributes.documentName
            txt_date.text = context?.getString(
                    R.string.last_used_at,
                    Util.getStringDateTimeWithServerDate(application.attributes.documentLastUsed))

            val cl = application.attributes.coverLetter

            if (cl != null && cl != "") {
                lay_cl.visibility = View.VISIBLE
                lbl_cover_letter.visibility = View.VISIBLE
                et_cover_letter.setText(application.attributes.coverLetter)
            } else {
                lay_cl.visibility = View.GONE
                lbl_cover_letter.visibility = View.GONE
            }
        }
    }

    private fun openDocSelector() {
        val mimeTypes = arrayOf(
//                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/pdf"
        )
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_CODE_DOC)
    }

    private fun showToast(msg: String) {
        Toast.makeText(context!!, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showProgress() = progressStatus(View.VISIBLE)

    private fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        dialogView.include_loading.visibility = viewStatus
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK) {
            handleDocSelectorActivityResult(data)
        }
    }

    private fun showRecentDocsDialog() {
        val dialog = RecentDocsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(DOC_ID, docId)
        outState.putString(JOB_ID, jobId)
        super.onSaveInstanceState(outState)
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    private fun handleDocSelectorActivityResult(data: Intent?) {
        val fileUri = data?.data
        if (fileUri != null)
            viewModel.checkDocumentAndUploadIt(fileUri)
        else
            showToast(R.string.select_valid_file)
    }

    override fun onDocumentClick(doc: DocumentResponse) {
        showDocData(doc)
    }

    private fun showDocData(doc: DocumentResponse?) {
        docId = doc?.id
        dialogView.also {
            it.btn_choose_recent_file.visibility = View.GONE
            it.lay_doc_detail.visibility = View.VISIBLE
            it.txt_filename.text = doc?.attributes?.name
            it.txt_date.text = context?.getString(
                    R.string.created_at,
                    Util.getStringDateTimeWithServerDate(doc?.attributes?.createdAt)
            )
        }
    }

    private fun showCancelDialogConfirm() {

        val dialog = CustomTwoBtnDialogFragment.newInstance(
                getString(R.string.cancel_application),
                getString(R.string.cancel_application_warning),
                getString(R.string.no),
                getString(R.string.yes)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                viewModel.cancelJobApplication(jobId)
            }

            override fun onPositiveBtnClick() {
            }
        })
        dialog.show(childFragmentManager, null)
    }

    interface Listener {

        fun onCancelJobApplication()
    }
}