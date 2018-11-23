package com.divercity.app.features.dialogs.jobapply

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast
import com.bumptech.glide.request.RequestOptions
import com.divercity.app.R
import com.divercity.app.core.base.BaseDialogFragment
import com.divercity.app.core.utils.GlideApp
import com.divercity.app.features.dialogs.JobSeekerActionsDialogFragment
import com.divercity.app.repository.user.UserRepository
import kotlinx.android.synthetic.main.dialog_job_apply.view.*
import java.io.File
import javax.inject.Inject


/**
 * Created by lucas on 18/03/2018.
 */

class JobApplyDialogFragment : BaseDialogFragment(), JobSeekerActionsDialogFragment.Listener {
    override fun onShareJobToGroups() {
    }

    override fun onReportJobPosting() {
    }

    override fun onShareJobViaMessage() {
    }

    var listener: Listener? = null

    @Inject
    lateinit var userRepository: UserRepository
    lateinit var viewModel : JobApplyDialogViewModel

    private var fileName: String? = null
    lateinit var dialogView: View

    var docId: String? = "-1"

    companion object {
        const val REQUEST_CODE_DOC = 150
        const val DOC_ID = "docId"

        fun newInstance(): JobApplyDialogFragment {
            return JobApplyDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.also {
            docId = it.getString(DOC_ID)
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory)[JobApplyDialogViewModel::class.java]
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
        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_job_apply, null)

        GlideApp.with(this)
                .load(userRepository.getAvatarUrl())
                .apply(RequestOptions().circleCrop())
                .into(dialogView.img_profile)

        dialogView.txt_fullname.text = userRepository.getFullName()

        dialogView.txt_usr_occupation.visibility = View.GONE
        dialogView.txt_usr_location.visibility = View.GONE

        dialogView.txt_filename.setOnClickListener {
            openDocSelector()
        }

        dialogView.btn_upload_new.setOnClickListener {
            openDocSelector()
        }

        dialogView.btn_submit_application.setOnClickListener {
            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
            showProgress()
            showDialogMoreActions()
        }

        dialogView.btn_close.setOnClickListener { dismiss() }

        builder.setView(dialogView)
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DOC && resultCode == Activity.RESULT_OK) {
            handleDocSelectorActivityResult(data)
        }
    }

    private fun openDocSelector() {
        val mimeTypes = arrayOf(
//                "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, REQUEST_CODE_DOC)
    }

    private fun handleDocSelectorActivityResult(data: Intent?) {
        val fileUri = data?.data
        val file = File(fileUri?.path)

        dialogView.also {
            it.btn_upload_new.visibility = View.GONE
            it.btn_choose_recent_file.visibility = View.GONE
            it.txt_filename.visibility = View.VISIBLE
            it.txt_filename.text = file.name
        }

        var iss = context?.contentResolver?.openInputStream(fileUri)

//        viewModel.uploadDocument(file.name, fileUri!!)


//        data?.data?.let { returnUri ->
//            val mime = MimeTypeMap.getSingleton()
//            val type = mime.getExtensionFromMimeType(context?.contentResolver?.getType(returnUri))
//            context?.contentResolver?.query(returnUri, null, null, null, null)
//        }?.use { cursor ->
//            /*
//             * Get the column indexes of the data in the Cursor,
//             * move to the first row in the Cursor, get the data,
//             * and display it.
//             */
//            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            cursor.moveToFirst()
//            fileName = cursor.getString(nameIndex)
//            cursor.close()
//
//            dialogView.also {
//                it.btn_upload_new.visibility = View.GONE
//                it.btn_choose_recent_file.visibility = View.GONE
//                it.txt_filename.visibility = View.VISIBLE
//                it.txt_filename.text = fileName
//            }
//        }
    }

    private fun showDialogMoreActions() {
        val dialog = JobSeekerActionsDialogFragment.newInstance()
        dialog.show(childFragmentManager, null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(DOC_ID, docId)
        super.onSaveInstanceState(outState)
    }

    interface Listener

    private fun showProgress() = progressStatus(View.VISIBLE)

    private fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int){
        dialogView.include_loading.visibility = viewStatus
    }


    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
            return uri.getPath()
        }// File
        // MediaStore (and general)
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                val index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.getAuthority()
    }
}