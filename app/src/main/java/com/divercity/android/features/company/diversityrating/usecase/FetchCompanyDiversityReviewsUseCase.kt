package com.divercity.android.features.company.diversityrating.usecase

import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewResponse
import com.divercity.android.repository.company.CompanyRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchCompanyDiversityReviewsUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository
) : UseCase<@JvmSuppressWildcards List<CompanyDiversityReviewResponse>, Params>(
    executorThread,
    uiThread
) {

    lateinit var companyId: String

    override fun createObservableUseCase(params: Params): Observable<List<CompanyDiversityReviewResponse>> {
        return repository.fetchCompanyDiversityReviews(
            companyId, params.page, params.size
        )
    }
}