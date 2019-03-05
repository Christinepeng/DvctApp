package com.divercity.android.features.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_home_tab_action.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class HomeTabActionDialogFragment : DialogFragment() {

    var listener: Listener? = null

    companion object {

        fun newInstance(): HomeTabActionDialogFragment {
            return HomeTabActionDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val builder = AlertDialog.Builder(activity!!)
        val dialogView =
            activity!!.layoutInflater.inflate(R.layout.dialog_home_tab_action, null)

        dialogView.apply {

            btn_new_group.setOnClickListener {
                listener?.onNewGroup()
                dismiss()
            }

            btn_new_topic.setOnClickListener {
                listener?.onNewTopic()
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

        fun onNewGroup()

        fun onNewTopic()
    }
}