package com.divercity.app.features.onboarding.selectschool.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.repository.onboarding.OnboardingRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchSchoolUseCase extends UseCase<DataArray<SchoolResponse>, FetchSchoolUseCase.Params> {

    private OnboardingRepository repository;

    @Inject
    public FetchSchoolUseCase(@Named("executor_thread") Scheduler executorThread,
                              @Named("ui_thread") Scheduler uiThread,
                              OnboardingRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<SchoolResponse>> createObservableUseCase(FetchSchoolUseCase.Params params) {
        return repository.fetchSchool(params.page, params.size, params.query);
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

        public static FetchSchoolUseCase.Params forSchool(int page, int size, String query) {
            return new FetchSchoolUseCase.Params(page, size, query);
        }
    }
}
