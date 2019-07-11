package com.divercity.android.features.picturessearch

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.Resource
import com.divercity.android.data.entity.photo.PhotoEntityResponse
import com.divercity.android.data.networking.config.DisposableObserverWrapper
import com.divercity.android.features.picturessearch.usecase.SearchPicturesUseCase
import com.google.gson.JsonElement
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class PictureSearchViewModel @Inject
constructor(
    private val searchPicturesUseCase: SearchPicturesUseCase
) : BaseViewModel() {

    val searchPicturesResponse = MutableLiveData<Resource<List<PhotoEntityResponse>>>()

    fun searchPictures(query: String) {
        searchPicturesResponse.postValue(Resource.loading(null))

        val callback = object : DisposableObserverWrapper<List<PhotoEntityResponse>>() {
            override fun onFail(error: String) {
                searchPicturesResponse.postValue(Resource.error(error, null))
            }

            override fun onHttpException(error: JsonElement) {
                searchPicturesResponse.postValue(Resource.error(error.toString(), null))
            }

            override fun onSuccess(o: List<PhotoEntityResponse>) {
                searchPicturesResponse.postValue(Resource.success(o))
            }
        }
        searchPicturesUseCase.execute(callback, SearchPicturesUseCase.Params.toSearch(query))
    }
}
