package com.divercity.app.repository.document

import android.content.Context
import android.net.Uri
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.document.DocumentResponse
import com.divercity.app.data.networking.services.DocumentService
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


/**
 * Created by lucas on 23/11/2018.
 */

class DocumentRepositoryImpl @Inject
constructor(
    private val context: Context,
    private val service: DocumentService
) : DocumentRepository {

    override fun uploadDocument(docName: String, uri: Uri): Observable<DataObject<DocumentResponse>> {
        val file = File(uri.path)

//        val doc = RequestBody.create(
//            MediaType.parse(context.contentResolver.getType(uri)!!),
//            file
//        )
        //Funca file android
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file.absoluteFile)
        val multi = MultipartBody.Part.createFormData("document[document]", file.name, requestFile)
        val name = RequestBody.create(MediaType.parse("text/plain"), docName)

//        val mime = MimeTypeMap.getSingleton()
//        val type = mime.getExtensionFromMimeType(context.contentResolver?.getType(uri))
//        val cursor = context.contentResolver?.query(uri, null, null, null, null)
//
//        var name =  RequestBody.create(MediaType.parse("text/plain"), "")
//        cursor?.use {
//            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            it.moveToFirst()
//            name = RequestBody.create(MediaType.parse("text/plain"), it.getString(nameIndex))
//            it.close()
//        }

        return service.uploadDocument(name, multi)
    }
}