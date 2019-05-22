package com.divercity.android.features.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.divercity.android.R
import kotlinx.android.synthetic.main.dialog_add_profile_picture.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class AddProfilePictureDialogFragment : DialogFragment() {

    var listener : Listener? = null

    companion object {

        fun newInstance(): AddProfilePictureDialogFragment {
            return AddProfilePictureDialogFragment()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_profile_picture, null)

        dialogView.apply {
            btn_add_picture.setOnClickListener {
                dismiss()
                listener?.onAddPicture()
            }

            btn_maybe_later.setOnClickListener {
                dismiss()
                listener?.onMaybeLater()
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

        fun onAddPicture()

        fun onMaybeLater()
    }
}