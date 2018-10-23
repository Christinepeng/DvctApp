package com.divercity.app.features.profile.selectindustry.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.repository.user.UserRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchIndustriesUseCase extends UseCase<DataArray<IndustryResponse>, FetchIndustriesUseCase.Params> {

    private UserRepository userRepository;

    @Inject
    public FetchIndustriesUseCase(@Named("executor_thread") Scheduler executorThread,
                                  @Named("ui_thread") Scheduler uiThread,
                                  UserRepository userRepository) {
        super(executorThread, uiThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<DataArray<IndustryResponse>> createObservableUseCase(FetchIndustriesUseCase.Params params) {
        return userRepository.fetchIndustries(params.page, params.size, params.query);
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

        public static FetchIndustriesUseCase.Params forIndustries(int page, int size, String query) {
            return new FetchIndustriesUseCase.Params(page, size, query);
        }
    }
}
