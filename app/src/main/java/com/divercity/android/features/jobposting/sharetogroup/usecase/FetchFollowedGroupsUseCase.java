package com.divercity.android.features.jobposting.sharetogroup.usecase;

import com.divercity.android.core.base.UseCase;
import com.divercity.android.data.entity.base.DataArray;
import com.divercity.android.data.entity.group.GroupResponse;
import com.divercity.android.repository.group.GroupRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchFollowedGroupsUseCase extends UseCase<DataArray<GroupResponse>, FetchFollowedGroupsUseCase.Params> {

    private GroupRepository repository;

    @Inject
    public FetchFollowedGroupsUseCase(@Named("executor_thread") Scheduler executorThread,
                                      @Named("ui_thread") Scheduler uiThread,
                                      GroupRepository repository) {
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
