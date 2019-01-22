package com.divercity.android.core.utils

import com.google.gson.JsonElement

/**
 * Created by lucas on 28/12/2018.
 */

object NetworkErrorsUtil {

    fun getErrorNode(json : JsonElement) : String{
        return json.asJsonObject.get("error").asString
    }

}