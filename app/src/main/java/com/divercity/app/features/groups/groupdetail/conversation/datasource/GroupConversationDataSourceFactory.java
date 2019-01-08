package com.divercity.app.features.groups.groupdetail.conversation.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.questions.QuestionResponse;
import com.divercity.app.features.groups.groupdetail.conversation.usecase.FetchGroupConversationsCase;

import io.reactivex.disposables.CompositeDisposable;

public class GroupConversationDataSourceFactory extends DataSource.Factory<Long, QuestionResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchGroupConversationsCase fetchGroupConversationsCase;
    private String query;

    private MutableLiveData<GroupConversationDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public GroupConversationDataSourceFactory(CompositeDisposable compositeDisposable,
                                              FetchGroupConversationsCase fetchGroupConversationsCase,
                                              @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchGroupConversationsCase = fetchGroupConversationsCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, QuestionResponse> create() {
        GroupConversationDataSource groupConversationDataSource = new GroupConversationDataSource(
                compositeDisposable,
                fetchGroupConversationsCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(groupConversationDataSource);
        return groupConversationDataSource;
    }

    @NonNull
    public MutableLiveData<GroupConversationDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
