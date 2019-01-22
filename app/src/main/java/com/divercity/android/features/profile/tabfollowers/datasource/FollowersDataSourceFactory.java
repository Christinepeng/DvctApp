package com.divercity.android.features.profile.tabfollowers.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.features.profile.tabfollowers.usecase.FetchFollowersUseCase;

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
