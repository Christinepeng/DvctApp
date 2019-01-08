package com.divercity.app.features.profile.tabfollowing.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.app.data.entity.user.response.UserResponse;
import com.divercity.app.features.profile.tabfollowing.usecase.FetchFollowingUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class FollowingDataSourceFactory extends DataSource.Factory<Long, UserResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchFollowingUseCase fetchFollowersUseCase;

    private MutableLiveData<FollowingDataSource> followersDataSourceMutableLiveData = new MutableLiveData<>();

    public FollowingDataSourceFactory(CompositeDisposable compositeDisposable,
                                      FetchFollowingUseCase fetchFollowersUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchFollowersUseCase = fetchFollowersUseCase;
    }

    @Override
    public DataSource<Long, UserResponse> create() {
        FollowingDataSource followersDataSource = new FollowingDataSource(
                compositeDisposable,
               fetchFollowersUseCase);
        followersDataSourceMutableLiveData.postValue(followersDataSource);
        return followersDataSource;
    }

    @NonNull
    public MutableLiveData<FollowingDataSource> getGroupsInterestsDataSource() {
        return followersDataSourceMutableLiveData;
    }

}
