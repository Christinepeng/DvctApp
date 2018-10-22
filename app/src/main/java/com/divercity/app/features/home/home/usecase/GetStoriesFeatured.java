package com.divercity.app.features.home.home.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse;
import com.divercity.app.repository.feed.FeedRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetStoriesFeatured extends UseCase<List<StoriesFeaturedResponse>,Void>  {

    private FeedRepository repository;

    @Inject
    public GetStoriesFeatured(@Named("executor_thread") Scheduler executorThread,
                              @Named("ui_thread") Scheduler uiThread,
                              FeedRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<List<StoriesFeaturedResponse>> createObservableUseCase(Void aVoid) {
        return repository.fetchStoresFeatured();
    }
}
