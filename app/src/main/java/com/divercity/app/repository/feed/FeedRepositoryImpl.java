package com.divercity.app.repository.feed;

import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.data.entity.storiesfeatured.StoriesFeaturedResponse;
import com.divercity.app.data.networking.services.FeedService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import io.reactivex.Observable;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by lucas on 18/10/2018.
 */

public class FeedRepositoryImpl implements FeedRepository {

    private FeedService feedService;

    @Inject
    public FeedRepositoryImpl(FeedService feedService) {
        this.feedService = feedService;
    }

    @Override
    public Observable<List<StoriesFeaturedResponse>> fetchStoresFeatured() {
        return feedService.fetchStoresFeatured().map(jsonObject -> {
            JsonArray rows = jsonObject.getAsJsonObject("data").getAsJsonArray("story");
            Gson gson = new Gson();
            List<StoriesFeaturedResponse> list = gson.fromJson(rows, new TypeToken<List<StoriesFeaturedResponse>>() {
            }.getType());
            return list;
        });
    }

    @Override
    public Observable<List<QuestionResponse>> fetchQuestions(int pageNumber, int size) {
        return feedService.fetchQuestions(pageNumber, size).map(jsonObject -> {
            JsonArray rows = jsonObject.getAsJsonArray("data");
            Gson gson = new Gson();
            List<QuestionResponse> questionResponseList =
                    gson.fromJson(rows, new TypeToken<List<QuestionResponse>>() {
                    }.getType());
            return questionResponseList;
        });
    }
}
