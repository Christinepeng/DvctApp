package com.divercity.android.features.company.ratecompany.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.rating.Rating
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewEntityResponse
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class UpdateCompanyReviewUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<CompanyDiversityReviewEntityResponse, UpdateCompanyReviewUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<CompanyDiversityReviewEntityResponse> {
        return repository.updateReview(params.companyId, params.rating)
    }

    class Params constructor(val companyId: String, val rating: Rating)
}