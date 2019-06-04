package com.divercity.android.core.base

import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by lucas on 2019-06-04.
 */

abstract class BaseRepository {

    protected fun checkResponse(response: Response<*>) {
        if (!response.isSuccessful)
            throw HttpException(response)
    }
}