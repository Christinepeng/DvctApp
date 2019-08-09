package com.divercity.android.features.company.ratecompany.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.review.CompanyDiversityReviewEntityResponse
import com.divercity.android.repository.company.CompanyRepository
import com.divercity.android.repository.session.SessionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class FetchReviewUseCase @Inject
constructor(
    @Named("executor_thread") executorThread: Scheduler,
    @Named("ui_thread") uiThread: Scheduler,
    private val repository: CompanyRepository,
    private val sessionRepository: SessionRepository
) : UseCase<CompanyDiversityReviewEntityResponse, FetchReviewUseCase.Params>(
    executorThread,
    uiThread
) {

    override fun createObservableUseCase(params: Params): Observable<CompanyDiversityReviewEntityResponse> {
        return repository.fetchReview(params.companyId, sessionRepository.getUserId())
    }

    class Params constructor(val companyId: String)
}