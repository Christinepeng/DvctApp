package com.divercity.android.features.company.diversityrating

import com.divercity.android.core.base.viewmodel.BaseViewModelPagination
import com.divercity.android.model.CompanyDiversityReview
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class DiversityRatingViewModel @Inject
constructor(
    repository: DiversityReviewsPaginatedRepository
) : BaseViewModelPagination<CompanyDiversityReview>(repository) {

    private var companyId: String? = null

    fun fetchCompanyDiversityReviews(companyId: String) {
        if (this.companyId == null) {
            this.companyId = companyId
            (repository as DiversityReviewsPaginatedRepository).setCompanyId(companyId)
            fetchData()
        }
    }
}