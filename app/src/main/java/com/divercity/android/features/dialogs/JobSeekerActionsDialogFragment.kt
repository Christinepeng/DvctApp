package com.divercity.android.features.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_job_seeker_actions.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class JobSeekerActionsDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        fun newInstance(): JobSeekerActionsDialogFragment {
            return JobSeekerActionsDialogFragment()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_seeker_actions, null)

        dialogView.btn_share_via_message.setOnClickListener {
            dismiss()
            listener?.onShareJobViaMessage()
        }

        dialogView.btn_share_to_groups.setOnClickListener {
            dismiss()
            listener?.onShareJobToGroups()
        }

        dialogView.btn_report_job_posting.setOnClickListener {
            dismiss()
            listener?.onReportJobPosting()
        }

        builder.setView(dialogView)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    interface Listener {

        fun onShareJobViaMessage()

        fun onShareJobToGroups()

        fun onReportJobPosting()
    }
}