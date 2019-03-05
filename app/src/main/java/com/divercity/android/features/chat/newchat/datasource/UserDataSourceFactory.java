package com.divercity.android.features.chat.newchat.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.divercity.android.features.chat.usecase.FetchUsersUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class UserDataSourceFactory extends DataSource.Factory<Long, Object> {

    private CompositeDisposable compositeDisposable;
    private FetchUsersUseCase fetchUsersUseCase;
    private String query;

    private MutableLiveData<UserDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public UserDataSourceFactory(CompositeDisposable compositeDisposable,
                                 FetchUsersUseCase fetchUsersUseCase,
                                 @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchUsersUseCase = fetchUsersUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, Object> create() {
        UserDataSource userDataSource = new UserDataSource(
                compositeDisposable,
                fetchUsersUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(userDataSource);
        return userDataSource;
    }

    @NonNull
    public MutableLiveData<UserDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
