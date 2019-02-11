package com.divercity.android.features.dialogs.groupaction

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_group_member_actions.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class GroupMemberActionsDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        fun newInstance(): GroupMemberActionsDialogFragment {
            return GroupMemberActionsDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)
        val dialogView =
            activity!!.layoutInflater.inflate(R.layout.dialog_group_member_actions, null)

        dialogView.apply {

            btn_write_new_post.setOnClickListener {

            }

            btn_invite.setOnClickListener {
                listener?.onInvite()
                dismiss()
            }
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

        fun onWriteNewPost()

        fun onInvite()

        fun onViewMembers()
    }
}