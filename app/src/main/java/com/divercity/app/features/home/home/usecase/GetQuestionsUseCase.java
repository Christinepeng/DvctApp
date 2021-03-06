package com.divercity.app.features.home.home.usecase;

import com.divercity.app.core.base.UseCase;
import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.repository.feed.FeedRepository;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

public class GetQuestionsUseCase extends UseCase<List<QuestionResponse>, GetQuestionsUseCase.Params> {

    private FeedRepository repository;

    @Inject
    public GetQuestionsUseCase(@Named("executor_thread") Scheduler executorThread,
                               @Named("ui_thread") Scheduler uiThread,
                               FeedRepository repository) {
        super(executorThread, uiThread);
        this.repository = repository;
    }

    @Override
    protected Observable<List<QuestionResponse>> createObservableUseCase(Params params) {
        return repository.fetchQuestions(params.page, params.size);
    }

    public static final class Params {

        private final int page;
        private final int size;

        private Params(int page, int size) {
            this.page = page;
            this.size = size;
        }

        public static Params forInterests(int page, int size) {
            return new Params(page, size);
        }
    }

}
