package com.divercity.app.repository.feed;

import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public interface FeedRepository {

    Observable<List<StoriesFeaturedResponse>> fetchStoresFeatured();

    Observable<List<QuestionResponse>> fetchQuestions(int pageNumber, int size);
}
