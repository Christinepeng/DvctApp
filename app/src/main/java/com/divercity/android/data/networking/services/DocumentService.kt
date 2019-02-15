package com.divercity.android.data.networking.services

import com.divercity.android.data.entity.base.DataArray
import com.divercity.android.data.entity.base.DataObject
import com.divercity.android.data.entity.document.DocumentResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Created by lucas on 23/11/2018.
 */
 
interface DocumentService {

    @Multipart
    @POST("user_documents")
    fun uploadDocument(@Part("document[name]") docName: RequestBody,
                @Part doc: MultipartBody.Part): Observable<Response<DataObject<DocumentResponse>>>

    @GET("user_documents")
    fun fetchRecentDocs(): Observable<Response<DataArray<DocumentResponse>>>
}