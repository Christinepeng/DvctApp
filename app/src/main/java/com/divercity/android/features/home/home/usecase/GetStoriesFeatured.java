package com.divercity.android.features.home.home.usecase;

import com.divercity.android.core.base.UseCase;
import com.divercity.android.data.entity.storiesfeatured.StoriesFeaturedResponse;
import com.divercity.android.repository.feed.FeedRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class GetStoriesFeatured extends UseCase<List<StoriesFeaturedResponse>,Void> {

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
