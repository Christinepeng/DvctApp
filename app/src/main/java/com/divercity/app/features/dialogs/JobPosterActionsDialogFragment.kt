package com.divercity.app.features.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.divercity.app.R
import kotlinx.android.synthetic.main.dialog_job_poster_actions.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class JobPosterActionsDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        fun newInstance(): JobPosterActionsDialogFragment {
            return JobPosterActionsDialogFragment()
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
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_poster_actions, null)

        dialogView.btn_edit_job.setOnClickListener {
            dismiss()
            listener?.onEditJobPosting()
        }

        dialogView.btn_unpublish_job.setOnClickListener {
            dismiss()
            listener?.onUnpublishJobPosting()
        }

        dialogView.btn_delete_job_posting.setOnClickListener {
            dismiss()
            listener?.onDeleteJobPosting()
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

        fun onEditJobPosting()

        fun onUnpublishJobPosting()

        fun onDeleteJobPosting()
    }
}