package com.divercity.android.features.dialogs.picture

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import kotlinx.android.synthetic.main.dialog_picture.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class PictureDialogFragment : DialogFragment() {

    private lateinit var dialogView: View

    companion object {

        private const val PARAM_URL = "paramUrl"

        fun newInstance(url : String): PictureDialogFragment {
            val fragment = PictureDialogFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_URL, url)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val builder = AlertDialog.Builder(requireActivity())
        dialogView = inflater.inflate(R.layout.dialog_picture, container)
        GlideApp.with(this)
            .load(arguments?.getString(PARAM_URL))
            .into(dialogView.img_full_screen)

        dialogView.btn_close.setOnClickListener {
            dismiss()
        }
        builder.setView(dialogView)
        return dialogView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            setLayout(width, height)
        }
    }
}