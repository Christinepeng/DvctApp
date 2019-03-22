package com.divercity.android.core.base

import com.divercity.android.core.utils.Listing

/**
 * Created by lucas on 01/10/2018.
 */

interface PaginatedQueryRepository<T> {

    fun fetchData(query: String?): Listing<T>

    fun retry()

    fun refresh()

    fun clear()

}
