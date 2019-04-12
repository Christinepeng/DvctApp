package com.divercity.android.features.home.people

import com.divercity.android.core.base.viewmodel.BaseViewModel
import javax.inject.Inject

class TabPeopleViewModel @Inject
constructor() : BaseViewModel() {

    var adapterPosition: Int? = null
    var lastSearchQuery: String = ""
}
