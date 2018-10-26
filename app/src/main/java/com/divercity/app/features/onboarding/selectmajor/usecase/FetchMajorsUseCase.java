package com.divercity.app.features.onboarding.selectmajor.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.repository.onboarding.OnboardingRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchMajorsUseCase extends UseCase<DataArray<MajorResponse>, FetchMajorsUseCase.Params> {

    private OnboardingRepository repository;

    @Inject
    public FetchMajorsUseCase(@Named("executor_thread") Scheduler executorThread,
                              @Named("ui_thread") Scheduler uiThread,
                              OnboardingRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<MajorResponse>> createObservableUseCase(FetchMajorsUseCase.Params params) {
        return repository.fetchMajors(params.page, params.size, params.query);
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

        public static FetchMajorsUseCase.Params forMajor(int page, int size, String query) {
            return new FetchMajorsUseCase.Params(page, size, query);
        }
    }
}
