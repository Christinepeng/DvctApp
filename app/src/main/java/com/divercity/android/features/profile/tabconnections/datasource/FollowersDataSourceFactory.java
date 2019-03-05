package com.divercity.android.features.profile.tabconnections.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;

import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.features.profile.tabconnections.usecase.FetchFollowersUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class FollowersDataSourceFactory extends DataSource.Factory<Long, UserResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchFollowersUseCase fetchFollowersUseCase;

    private MutableLiveData<FollowersDataSource> followersDataSourceMutableLiveData = new MutableLiveData<>();

    public FollowersDataSourceFactory(CompositeDisposable compositeDisposable,
                                      FetchFollowersUseCase fetchFollowersUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchFollowersUseCase = fetchFollowersUseCase;
    }

    @Override
    public DataSource<Long, UserResponse> create() {
       FollowersDataSource followersDataSource = new FollowersDataSource(
                compositeDisposable,
               fetchFollowersUseCase);
        followersDataSourceMutableLiveData.postValue(followersDataSource);
        return followersDataSource;
    }

    @NonNull
    public MutableLiveData<FollowersDataSource> getGroupsInterestsDataSource() {
        return followersDataSourceMutableLiveData;
    }

}
