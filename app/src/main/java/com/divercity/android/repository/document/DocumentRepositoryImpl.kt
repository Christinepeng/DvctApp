package com.divercity.android.repository.document

import com.divercity.android.core.base.BaseRepository
import com.divercity.android.data.entity.document.DocumentResponse
import com.divercity.android.data.networking.services.DocumentService
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
    private val service: DocumentService
) : BaseRepository(), DocumentRepository {

    override fun fetchRecentDocs(): Observable<List<DocumentResponse>> {
        return service.fetchRecentDocs().map {
            checkResponse(it)
            it.body()?.data
        }
    }

    override fun uploadDocument(file: File): Observable<DocumentResponse> {

        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file.absoluteFile)
        val multi = MultipartBody.Part.createFormData("document[document]", file.name, requestFile)
        val name = RequestBody.create(MediaType.parse("text/plain"), file.name)

        return service.uploadDocument(name, multi).map {
            checkResponse(it)
            it.body()?.data
        }
    }
}