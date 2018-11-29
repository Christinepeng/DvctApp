package com.divercity.app.features.groups.onboarding.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.repository.data.DataRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchGroupsUseCase extends UseCase<DataArray<GroupResponse>, FetchGroupsUseCase.Params> {

    private DataRepository repository;

    @Inject
    public FetchGroupsUseCase(@Named("executor_thread") Scheduler executorThread,
                              @Named("ui_thread") Scheduler uiThread,
                              DataRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<GroupResponse>> createObservableUseCase(FetchGroupsUseCase.Params params) {
        return repository.fetchGroups(params.page, params.size, params.query);
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

        public static FetchGroupsUseCase.Params forGroups(int page, int size, String query) {
            return new FetchGroupsUseCase.Params(page, size, query);
        }
    }
}
