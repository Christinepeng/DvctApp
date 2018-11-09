package com.divercity.app.features.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.divercity.app.R
import com.divercity.app.core.ui.AnimateHorizontalProgressBar

/**
 * Created by lucas on 18/03/2018.
 */

class AddProfilePictureDialogFragment : DialogFragment() {

    companion object {

        fun newInstance(): AddProfilePictureDialogFragment {
            return AddProfilePictureDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_complete_profile, null)

        val progressBar = dialogView.findViewById<AnimateHorizontalProgressBar>(R.id.progress_bar)
        progressBar.max = 100
        progressBar.progress = 0
        progressBar.setProgressWithAnim(100)

        val progressText = dialogView.findViewById<TextView>(R.id.txt_progress)
        progressText.text = "100%"

        dialogView.findViewById<View>(R.id.dlg_cmpl_prf_btn_thanks).setOnClickListener { _ -> dismiss() }
        builder.setView(dialogView)
        return builder.create()
    }
}