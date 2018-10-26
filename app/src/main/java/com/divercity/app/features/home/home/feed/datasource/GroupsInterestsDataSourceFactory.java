package com.divercity.app.features.home.home.feed.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.features.home.home.usecase.GetQuestionsUseCase;
import io.reactivex.disposables.CompositeDisposable;

public class GroupsInterestsDataSourceFactory extends DataSource.Factory<Long, QuestionResponse> {

    private CompositeDisposable compositeDisposable;
    private GetQuestionsUseCase getQuestionsUseCase;

    private MutableLiveData<GroupsInterestsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public GroupsInterestsDataSourceFactory(CompositeDisposable compositeDisposable,
                                            GetQuestionsUseCase getQuestionsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.getQuestionsUseCase = getQuestionsUseCase;
    }

    @Override
    public DataSource<Long, QuestionResponse> create() {
        GroupsInterestsDataSource groupsInterestsDataSource = new GroupsInterestsDataSource(
                compositeDisposable,
                getQuestionsUseCase);
        mGroupsInterestsDataSourceMutableLiveData.postValue(groupsInterestsDataSource);
        return groupsInterestsDataSource;
    }

    @NonNull
    public MutableLiveData<GroupsInterestsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
