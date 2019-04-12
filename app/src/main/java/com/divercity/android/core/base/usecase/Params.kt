package com.divercity.android.core.base.usecase

/**
 * Created by lucas on 11/04/2019.
 */

class Params(val page: Int, val size: Int, query: String?) {

    val query: String? = query
//        This is because if you send searchQuery = "" to the backend, it doesn't work,
//        when value is "" we must send null
        get() = if (field != null && field == "") null else field
}