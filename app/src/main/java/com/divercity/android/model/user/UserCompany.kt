package com.divercity.android.model.user

/**
 * Created by lucas on 2019-04-24.
 */

data class UserCompany(
    val name: String? = null,
    val id: Int? = null
) {
    companion object {
        fun empty() = UserCompany()
    }
}