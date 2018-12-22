package com.divercity.app.features.jobposting.sharetogroup.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.repository.data.DataRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchFollowedGroupsUseCase extends UseCase<DataArray<GroupResponse>, FetchFollowedGroupsUseCase.Params> {

    private DataRepository repository;

    @Inject
    public FetchFollowedGroupsUseCase(@Named("executor_thread") Scheduler executorThread,
                                      @Named("ui_thread") Scheduler uiThread,
                                      DataRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<GroupResponse>> createObservableUseCase(Params params) {
        return repository.fetchFollowedGroups(
                params.page,
                params.size,
                params.query.equals("") ? null : params.query);
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

        public static Params forGroups(int page, int size, String query) {
            return new Params(page, size, query);
        }
    }
}
