package com.divercity.android.features.dialogs.jobsearchfilter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseDialogFragment
import com.divercity.android.features.dialogs.jobsearchfilter.search.searchfilter.JobSearchFilterFragment

/**
 * Created by lucas on 18/03/2018.
 */

class JobSearchFilterDialogFragment : BaseDialogFragment(), IJobSearchFilter {

    private lateinit var dialogView: View
    lateinit var viewModel: JobSearchFilterDialogViewModel

    companion object {

        fun newInstance(): JobSearchFilterDialogFragment {
            return JobSearchFilterDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
        viewModel = ViewModelProviders.of(requireActivity(), viewModelFactory)[JobSearchFilterDialogViewModel::class.java]
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
            .replace(R.id.fragment_container, JobSearchFilterFragment.newInstance())
            .commit()
    }

    override fun replaceFragment(fragment: Fragment?, tag: String?) {
        if(fragment != null) {
            childFragmentManager
                .beginTransaction()
                .addToBackStack(tag)
                .replace(R.id.fragment_container, fragment)
                .commit()
        } else {
            dismiss()
        }
    }

    override fun onBackPressed() {
//        hideKeyboard()
        childFragmentManager.popBackStackImmediate()
    }

    fun hideKeyboard() {
        view?.run {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}