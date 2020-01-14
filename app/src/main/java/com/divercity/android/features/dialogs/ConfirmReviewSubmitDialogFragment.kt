package com.divercity.android.features.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_write_review_warning.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class ConfirmReviewSubmitDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {
        fun newInstance() = ConfirmReviewSubmitDialogFragment()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val dialogView =
            requireActivity().layoutInflater.inflate(R.layout.dialog_review_submit_confirm, null)

        dialogView.apply {
            btn_continue_review.setOnClickListener { dismiss() }
            btn_submit.setOnClickListener { listener?.onSubmit() }
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