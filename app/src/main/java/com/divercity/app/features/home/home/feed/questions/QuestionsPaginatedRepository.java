package com.divercity.app.features.home.home.feed.questions;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.questions.QuestionResponse;

/**
 * Created by lucas on 01/10/2018.
 */

public interface QuestionsPaginatedRepository {

    Listing<QuestionResponse> fetchQuestions();

    void retry();

    void refresh();

    void clear();

}
