package com.divercity.android.features.onboarding.selectoccupationofinterests.usecase;

import com.divercity.android.core.base.UseCase;
import com.divercity.android.data.entity.occupationofinterests.OOIResponse;
import com.divercity.android.repository.data.DataRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchOOIUseCase extends UseCase<List<OOIResponse>, FetchOOIUseCase.Params> {

    private DataRepository repository;

    @Inject
    public FetchOOIUseCase(@Named("executor_thread") Scheduler executorThread,
                           @Named("ui_thread") Scheduler uiThread,
                           DataRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<List<OOIResponse>> createObservableUseCase(Params params) {
        return repository.fetchOccupationOfInterests(params.page, params.size, params.query);
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

        public static Params forMajor(int page, int size, String query) {
            return new Params(page, size, query);
        }
    }
}
