package com.divercity.app.features.profile.selectcompany.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchCompaniesUseCase extends UseCase<DataArray<CompanyResponse>, FetchCompaniesUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public FetchCompaniesUseCase(@Named("executor_thread") Scheduler executorThread,
                                       @Named("ui_thread") Scheduler uiThread,
                                       UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<DataArray<CompanyResponse>> createObservableUseCase(FetchCompaniesUseCase.Params params) {
        return userRepository.fetchCompanies(params.page, params.size, params.query);
    }

    public static final class Params {

        private final int page;
        private final int size;
        private final String query;

        private Params(int page, int size, String query) {
            this.page = page;
            this.size = size;
            this.query = query;
        }

        public static FetchCompaniesUseCase.Params forCompanies(int page, int size, String query) {
            return new FetchCompaniesUseCase.Params(page, size, query);
        }
    }
}
