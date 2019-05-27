package com.divercity.android.features.dialogs.jobsearchfilter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.divercity.android.R
import com.divercity.android.core.base.BaseDialogFragment
import com.divercity.android.features.jobs.jobs.search.JobSearchFilterFragment

/**
 * Created by lucas on 18/03/2018.
 */

class JobSearchFilterDialogFragment : BaseDialogFragment() {

    private lateinit var dialogView: View

    companion object {

        fun newInstance(): JobSearchFilterDialogFragment {
            return JobSearchFilterDialogFragment()
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
        dialogView = inflater.inflate(R.layout.view_search_filter, container)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState ?: childFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, JobSearchFilterFragment.newInstance())
            .commit()
    }
}