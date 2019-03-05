package com.divercity.android.features.chat.recentchats.oldrecentchats.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.divercity.android.data.entity.chat.currentchats.ExistingUsersChatListItem;
import com.divercity.android.features.chat.recentchats.usecase.FetchCurrentChatsUseCase;

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
