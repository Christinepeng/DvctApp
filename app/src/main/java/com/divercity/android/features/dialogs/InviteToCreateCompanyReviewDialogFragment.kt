package com.divercity.android.features.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_create_review_invitation.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class InviteToCreateCompanyReviewDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {
        fun newInstance() = InviteToCreateCompanyReviewDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val dialogView =
            requireActivity().layoutInflater.inflate(R.layout.dialog_create_review_invitation, null)

        dialogView.apply {
            btn_get_start_review.setOnClickListener {
                Log.d("ReviewStatus", "get start button")
                listener?.onSubmit()
            }
            btn_maybe_later.setOnClickListener {
                Log.d("ReviewStatus", "maybe later button")
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

        fun onSubmit()
    }
}