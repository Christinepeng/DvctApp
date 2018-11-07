package com.divercity.app.features.company.base.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.repository.data.DataRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchCompaniesUseCase extends UseCase<DataArray<CompanyResponse>, FetchCompaniesUseCase.Params> {

    private DataRepository repository;

    @Inject
    public FetchCompaniesUseCase(@Named("executor_thread") Scheduler executorThread,
                                 @Named("ui_thread") Scheduler uiThread,
                                 DataRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<CompanyResponse>> createObservableUseCase(FetchCompaniesUseCase.Params params) {
        return repository.fetchCompanies(params.page, params.size, params.query);
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
