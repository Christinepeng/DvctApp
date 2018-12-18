package com.divercity.app.features.profile.tabfollowers.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.features.profile.tabfollowers.usecase.FetchFollowersUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class FollowersDataSourceFactory extends DataSource.Factory<Long, LoginResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchFollowersUseCase fetchFollowersUseCase;

    private MutableLiveData<FollowersDataSource> followersDataSourceMutableLiveData = new MutableLiveData<>();

    public FollowersDataSourceFactory(CompositeDisposable compositeDisposable,
                                      FetchFollowersUseCase fetchFollowersUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchFollowersUseCase = fetchFollowersUseCase;
    }

    @Override
    public DataSource<Long, LoginResponse> create() {
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
