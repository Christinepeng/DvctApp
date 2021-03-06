package com.divercity.app.features.location.base.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.location.LocationResponse;
import com.divercity.app.repository.data.DataRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchLocationsUseCase extends UseCase<DataArray<LocationResponse>, FetchLocationsUseCase.Params> {

    private DataRepository repository;

    @Inject
    public FetchLocationsUseCase(@Named("executor_thread") Scheduler executorThread,
                                 @Named("ui_thread") Scheduler uiThread,
                                 DataRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<LocationResponse>> createObservableUseCase(Params params) {
        return repository.fetchLocations(params.page, params.size, params.query);
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

        public static Params forSchool(int page, int size, String query) {
            return new Params(page, size, query);
        }
    }
}
