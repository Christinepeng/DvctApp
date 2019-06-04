package com.divercity.android.features.dialogs.jobsearchfilter

import androidx.lifecycle.MutableLiveData
import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import com.divercity.android.data.entity.industry.IndustryResponse
import com.divercity.android.data.entity.location.LocationResponse
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class JobSearchFilterDialogViewModel @Inject
constructor() : BaseViewModel() {

    var viewTypeFilter = MutableLiveData<String>()

    var locationFilter = MutableLiveData<String>()
    var selectedLocations = HashSet<LocationResponse>()
    var isAllLocationSelected = true

    var companySize = MutableLiveData<String>()
    var selectedSizes = HashSet<CompanySizeResponse>()
    var isAllSizesSelected = true

    var companyIndustry = MutableLiveData<String>()
    var selectedIndustries = HashSet<IndustryResponse>()
    var isAllIndustriesSelected = true

    init {
        /* Filters */
        viewTypeFilter.value = "All"
        locationFilter.value = "All"
        companySize.value = "All Sizes"
        companyIndustry.value = "All"
    }
}
