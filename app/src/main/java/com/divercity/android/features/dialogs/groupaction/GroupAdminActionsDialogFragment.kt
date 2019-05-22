package com.divercity.android.features.dialogs.groupaction

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_group_admin_actions.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class GroupAdminActionsDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        fun newInstance(): GroupAdminActionsDialogFragment {
            return GroupAdminActionsDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(requireActivity())
        val dialogView =
            requireActivity().layoutInflater.inflate(R.layout.dialog_group_admin_actions, null)

        dialogView.apply {

            btn_invite.setOnClickListener {
                listener?.onInvite()
                dismiss()
            }

            btn_add_admin.setOnClickListener {
                listener?.onAddAdmin()
                dismiss()
            }

            btn_edit_admin.setOnClickListener {
                listener?.onEditAdmin()
                dismiss()
            }

            btn_edit_group.setOnClickListener {
                listener?.onEditGroup()
                dismiss()
            }

            btn_edit_members.setOnClickListener {
                listener?.onEditMembers()
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

        fun onInvite()

        fun onAddAdmin()

        fun onEditAdmin()

        fun onEditGroup()

        fun onEditMembers()
    }
}