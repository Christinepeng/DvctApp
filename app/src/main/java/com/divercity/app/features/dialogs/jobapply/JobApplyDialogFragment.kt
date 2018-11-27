package com.divercity.app.features.dialogs.jobapply

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
import com.divercity.app.data.Resource
import com.divercity.app.data.Status
import com.divercity.app.features.dialogs.recentdocuments.RecentDocsDialogFragment
import com.divercity.app.repository.user.UserRepository
import kotlinx.android.synthetic.main.dialog_job_apply.view.*
import javax.inject.Inject


/**
 * Created by lucas on 18/03/2018.
 */

class JobApplyDialogFragment : BaseDialogFragment(), RecentDocsDialogFragment.Listener {

    var listener: Listener? = null

    @Inject
    lateinit var userRepository: UserRepository
    lateinit var viewModel: JobApplyDialogViewModel

    private lateinit var dialogView: View

    var docId: String? = "-1"

    companion object {
        private const val JOB_ID = "jobId"
        const val REQUEST_CODE_DOC = 150
        const val DOC_ID = "docId"

        fun newInstance(jobId : String): JobApplyDialogFragment {
            val fragment = JobApplyDialogFragment()
            val arguments = Bundle()
            arguments.putString(JOB_ID, jobId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.also {
            docId = it.getString(DOC_ID)
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory)[JobApplyDialogViewModel::class.java]
        subscribeToLiveData()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_apply, null)

        GlideApp.with(this)
                .load(userRepository.getAvatarUrl())
                .apply(RequestOptions().circleCrop())
                .into(dialogView.img_profile)

        dialogView.txt_fullname.text = userRepository.getFullName()

        dialogView.txt_usr_occupation.visibility = View.GONE
        dialogView.txt_usr_location.visibility = View.GONE

        dialogView.txt_filename.setOnClickListener {
            openDocSelector()
        }

        dialogView.btn_upload_new.setOnClickListener {
            openDocSelector()
        }

        dialogView.btn_choose_recent_file.setOnClickListener {
            showRecentDocsDialog()
        }

        dialogView.btn_submit_application.setOnClickListener {
            viewModel.uploadDocumentResponse.postValue(Resource.error(context!!.getString(R.string.select_valid_file), null))
        }

        dialogView.btn_close.setOnClickListener { dismiss() }

        builder.setView(dialogView)
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
                    dialogView.also {
                        it.btn_upload_new.visibility = View.GONE
                        it.btn_choose_recent_file.visibility = View.GONE
                        it.txt_filename.visibility = View.VISIBLE
                        it.txt_filename.text = document.data?.attributes?.name
                    }
                }
            }
        })
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

    override fun onDocumentClick(docId: String) {

    }

    interface Listener

}