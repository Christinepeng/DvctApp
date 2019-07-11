package com.divercity.android.features.company.createcompany.usecase

import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.data.entity.company.createcompanybody.Company
import com.divercity.android.data.entity.company.createcompanybody.CreateCompanyBody
import com.divercity.android.repository.company.CompanyRepository
import com.divercity.android.repository.user.UserRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by lucas on 18/10/2018.
 */

class CreateCompanyUseCase @Inject
constructor(
        @Named("executor_thread") executorThread: Scheduler,
        @Named("ui_thread") uiThread: Scheduler,
        private val repository: CompanyRepository,
        private val userRepository: UserRepository
) : UseCase<Boolean, CreateCompanyUseCase.Params>(executorThread, uiThread) {

    override fun createObservableUseCase(params: Params): Observable<Boolean> {
        val company = Company(
                name = params.name,
                userCompanySizeId = params.sizeId,
                description = params.desc,
                headquarters = params.headquarters,
                industryId = params.industryId,
                logo = params.logo
        )
        return repository.createCompany(CreateCompanyBody(company))
    }

    class Params private constructor(
            val name: String,
            val sizeId: String,
            val desc: String,
            val headquarters: String,
            val industryId: String,
            val logo: String
    ) {

        companion object {

            fun forCompany(
                    name: String,
                    sizeId: String,
                    desc: String,
                    headquarters: String,
                    industryId: String,
                    logo: String): Params {
                return Params(name, sizeId, desc, headquarters, industryId, logo)
            }
        }
    }
}
