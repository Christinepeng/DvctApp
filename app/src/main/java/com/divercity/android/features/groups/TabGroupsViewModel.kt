package com.divercity.android.features.groups

import com.divercity.android.core.base.viewmodel.BaseViewModel
import javax.inject.Inject

class TabGroupsViewModel @Inject
constructor() : BaseViewModel() {

    var adapterPosition: Int? = null
    var lastSearchQuery: String = ""
}
