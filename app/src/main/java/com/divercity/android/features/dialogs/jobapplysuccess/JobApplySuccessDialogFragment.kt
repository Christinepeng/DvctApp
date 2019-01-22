package com.divercity.android.features.dialogs.jobapplysuccess

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.core.base.BaseDialogFragment
import com.divercity.android.features.jobs.similarjobs.SimilarJobListFragment
import kotlinx.android.synthetic.main.dialog_job_apply_success.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class JobApplySuccessDialogFragment : BaseDialogFragment() {

    private lateinit var dialogView: View

    private var jobId: String? = null

    companion object {
        private const val JOB_ID = "jobId"

        fun newInstance(jobId: String): JobApplySuccessDialogFragment {
            val fragment = JobApplySuccessDialogFragment()
            val arguments = Bundle()
            arguments.putString(JOB_ID, jobId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobId = arguments?.getString(JOB_ID)
        savedInstanceState?.also {
            jobId = it.getString(JOB_ID)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_apply_success, null)

        dialogView.apply {

            btn_close.setOnClickListener {
                dismiss()
            }

            builder.setView(this)
        }
        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return dialogView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState ?: childFragmentManager
            .beginTransaction()
            .add(R.id.list_container, SimilarJobListFragment.newInstance(jobId!!))
            .commit()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}