package com.divercity.android.features.dialogs.invitegroup

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.group.GroupResponse
import kotlinx.android.synthetic.main.dialog_invite_group.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class InviteGroupDialogFragment : DialogFragment() {

    var listener: Listener? = null

    var group: GroupResponse? = null

    companion object {

        private const val PARAM_GROUP = "paramGroup"

        fun newInstance(group: GroupResponse): InviteGroupDialogFragment {
            val fragment = InviteGroupDialogFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_GROUP, group)
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
        group = arguments?.getParcelable(PARAM_GROUP)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_invite_group, null)

        dialogView.apply {

            GlideApp.with(this)
                .load(group?.attributes?.pictureMain)
                .into(img_group)

            btn_invite_contact.setOnClickListener {
                dismiss()
                listener?.onInviteContact()
            }

            btn_invite_divercity.setOnClickListener {
                dismiss()
                listener?.onInviteDivercity()
            }

            btn_not_now.setOnClickListener {
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

        fun onInviteContact()

        fun onInviteDivercity()
    }
}