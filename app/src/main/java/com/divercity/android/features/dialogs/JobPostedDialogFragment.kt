package com.divercity.android.features.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_job_posted.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class JobPostedDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        fun newInstance(): JobPostedDialogFragment {
            return JobPostedDialogFragment()
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
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_posted, null)

        dialogView.btn_share_to_friends.setOnClickListener {
            listener?.onShareToFriendsClick()
        }

        dialogView.btn_share_to_groups.setOnClickListener {
            listener?.onShareToGroupsClick()
        }

        dialogView.btn_close.setOnClickListener {
            listener?.onBtnCloseClick()
            dismiss()
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

        fun onShareToGroupsClick()

        fun onShareToFriendsClick()

        fun onBtnCloseClick()
    }
}