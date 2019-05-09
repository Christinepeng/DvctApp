package com.divercity.android.features.search

import com.divercity.android.core.base.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class SearchViewModel @Inject
constructor() : BaseViewModel() {

    var lastSearchQuery: String? = ""

}
