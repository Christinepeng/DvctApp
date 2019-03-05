package com.divercity.android.features.profile.tabfollowing.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;

import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.features.profile.tabfollowing.usecase.FetchFollowingUseCase;

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
