package com.divercity.android.features.dialogs.recentdocuments

import android.app.Dialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseDialogFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.document.DocumentResponse
import kotlinx.android.synthetic.main.dialog_recent_docs.view.*
import javax.inject.Inject


/**
 * Created by lucas on 18/03/2018.
 */

class RecentDocsDialogFragment : BaseDialogFragment() {

    var listener: Listener? = null

    @Inject
    lateinit var adapter: RecentDocsDialogsAdapter

    lateinit var viewModel: RecentDocsDialogViewModel

    private lateinit var dialogView: View

    companion object {

        fun newInstance(): RecentDocsDialogFragment {
            return RecentDocsDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[RecentDocsDialogViewModel::class.java]
        subscribeToLiveData()
        viewModel.fetchRecentDocs()
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
        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_recent_docs, null)

        adapter.listener = listenerAdapter
        dialogView.list_jobs_questions.adapter = adapter

        builder.setView(dialogView)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchRecentDocumentsResponse.observe(this, Observer { document ->
            when (document?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    showToast(document.message ?: "Error")
                }

                Status.SUCCESS -> {
                    hideProgress()
                    if(document.data?.size == 0)
                        showNoRecentFiles(View.VISIBLE)
                    else {
                        showNoRecentFiles(View.GONE)
                        adapter.submitList(document.data)
                    }
                }
            }
        })
    }

    private fun showNoRecentFiles(visibility : Int){
        dialogView.txt_no_recent_file.visibility = visibility
    }

    private fun showToast(msg: String) {
        Toast.makeText(context!!, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showProgress() = progressStatus(View.VISIBLE)

    private fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        dialogView.include_loading.visibility = viewStatus
    }

    interface Listener {
        fun onDocumentClick(doc: DocumentResponse)
    }

    private val listenerAdapter = object : RecentDocsDialogsAdapter.Listener{

        override fun onDocClick(doc: DocumentResponse) {
            listener?.onDocumentClick(doc)
            dismiss()
        }
    }
}