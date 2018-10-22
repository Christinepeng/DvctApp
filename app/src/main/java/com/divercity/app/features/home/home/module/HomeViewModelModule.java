package com.divercity.app.features.home.home.module;

import com.divercity.app.features.home.home.feed.questions.QuestionsPaginatedRepository;
import com.divercity.app.features.home.home.feed.questions.QuestionsPaginatedRepositoryImpl;

import dagger.Binds;
import dagger.Module;

/**
 * Created by lucas on 01/10/2018.
 */

@Module
public abstract class HomeViewModelModule {

    @Binds
    public abstract QuestionsPaginatedRepository bindQuestionsRepository(QuestionsPaginatedRepositoryImpl questionsRepository);

}
