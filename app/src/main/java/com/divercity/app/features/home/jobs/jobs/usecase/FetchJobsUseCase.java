package com.divercity.app.features.home.jobs.jobs.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.job.JobResponse;
import com.divercity.app.repository.job.JobRepository;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by lucas on 18/10/2018.
 */

public class FetchJobsUseCase extends UseCase<DataArray<JobResponse>, FetchJobsUseCase.Params> {

    private JobRepository repository;

    @Inject
    public FetchJobsUseCase(@Named("executor_thread") Scheduler executorThread,
                            @Named("ui_thread") Scheduler uiThread,
                            JobRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<DataArray<JobResponse>> createObservableUseCase(Params params) {
        return repository.fetchJobs(params.page, params.size);
    }

    public static final class Params {

        private final int page;
        private final int size;

        private Params(int page, int size) {
            this.page = page;
            this.size = size;
        }

        public static Params forJobs(int page, int size) {
            return new Params(page, size);
        }
    }
}
