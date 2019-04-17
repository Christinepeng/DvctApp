package com.divercity.android.features.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_company_actions.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class CompanyActionsDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        private const val PARAM_COMPANY_IS_ADMIN = "paramCompanyIsAdmin"

        fun newInstance(isAdmin: Boolean): CompanyActionsDialogFragment {
            val fragment = CompanyActionsDialogFragment()
            val arguments = Bundle()
            arguments.putBoolean(PARAM_COMPANY_IS_ADMIN, isAdmin)
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_company_actions, null)

        if(arguments!!.getBoolean(PARAM_COMPANY_IS_ADMIN, false)){
            dialogView.btn_edit_admins.setOnClickListener {
                dismiss()
                listener?.onEditAdmins()
            }
        } else {
            dialogView.btn_edit_admins.visibility = View.GONE
        }

        dialogView.btn_share_via_message.setOnClickListener {
            dismiss()
            listener?.onShareJobViaMessage()
        }

        dialogView.btn_share_to_groups.setOnClickListener {
            dismiss()
            listener?.onShareJobToGroups()
        }

        dialogView.btn_report_company.setOnClickListener {
            dismiss()
            listener?.onReportCompany()
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

        fun onReportCompany()

        fun onEditAdmins()
    }
}