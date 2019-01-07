package com.divercity.app.features.chat.recentchats.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.app.data.entity.chat.currentchats.ExistingUsersChatListItem;
import com.divercity.app.features.chat.recentchats.usecase.FetchCurrentChatsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class ChatsDataSourceFactory extends DataSource.Factory<Long, ExistingUsersChatListItem> {

    private CompositeDisposable compositeDisposable;
    private FetchCurrentChatsUseCase fetchCurrentChatsUseCase;
    private String query;

    private MutableLiveData<ChatsDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public ChatsDataSourceFactory(CompositeDisposable compositeDisposable,
                                  FetchCurrentChatsUseCase fetchCurrentChatsUseCase,
                                  @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchCurrentChatsUseCase = fetchCurrentChatsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, ExistingUsersChatListItem> create() {
        ChatsDataSource chatsDataSource = new ChatsDataSource(
                compositeDisposable,
                fetchCurrentChatsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(chatsDataSource);
        return chatsDataSource;
    }

    @NonNull
    public MutableLiveData<ChatsDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
