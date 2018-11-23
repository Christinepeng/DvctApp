package com.divercity.app.repository.document

import android.net.Uri
import com.divercity.app.data.entity.base.DataObject
import com.divercity.app.data.entity.document.DocumentResponse
import io.reactivex.Observable

/**
 * Created by lucas on 23/11/2018.
 */
 
interface DocumentRepository {

    fun uploadDocument(docName: String,
                       uri: Uri
    ): Observable<DataObject<DocumentResponse>>
}