package com.divercity.app.repository.document

import com.divercity.app.data.entity.document.DocumentResponse
import io.reactivex.Observable
import java.io.File

/**
 * Created by lucas on 23/11/2018.
 */
 
interface DocumentRepository {

    fun uploadDocument(file: File): Observable<DocumentResponse>

    fun fetchRecentDocs() : Observable<List<DocumentResponse>>
}