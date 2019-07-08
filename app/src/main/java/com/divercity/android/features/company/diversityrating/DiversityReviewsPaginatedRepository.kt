package com.divercity.android.features.company.diversityrating

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.company.diversityrating.usecase.FetchCompanyDiversityReviewsUseCase
import com.divercity.android.model.CompanyDiversityReview
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class DiversityReviewsPaginatedRepository @Inject
constructor(private val fetchCompanyDiversityReviewsUseCase: FetchCompanyDiversityReviewsUseCase) :
    BaseDataSourceRepository<CompanyDiversityReview>() {

    override fun getUseCase(): UseCase<List<CompanyDiversityReview>, Params> {
        return fetchCompanyDiversityReviewsUseCase
    }

    fun setCompanyId(companyId: String) {
        fetchCompanyDiversityReviewsUseCase.companyId = companyId
    }
}
