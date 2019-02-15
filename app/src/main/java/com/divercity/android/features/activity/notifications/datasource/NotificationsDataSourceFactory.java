package com.divercity.android.features.activity.notifications.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.features.activity.notifications.usecase.FetchNotificationsUseCase;
import com.divercity.android.features.profile.tabfollowing.datasource.FollowingDataSource;
import com.divercity.android.features.profile.tabfollowing.usecase.FetchFollowingUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class NotificationsDataSourceFactory extends DataSource.Factory<Long, UserResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchNotificationsUseCase fetchNotificationsUseCase;

    private MutableLiveData<NotificationsDataSource> notificationsDataSourceMutableLiveData = new MutableLiveData<>();

    public NotificationsDataSourceFactory(CompositeDisposable compositeDisposable,
                                          FetchNotificationsUseCase fetchNotificationsUseCase) {
        this.compositeDisposable = compositeDisposable;
        this.fetchNotificationsUseCase = fetchNotificationsUseCase;
    }

    @Override
    public DataSource<Long, UserResponse> create() {
        NotificationsDataSource followersDataSource = new NotificationsDataSource(
                compositeDisposable,
                fetchNotificationsUseCase);
        notificationsDataSourceMutableLiveData.postValue(followersDataSource);
        return followersDataSource;
    }

    @NonNull
    public MutableLiveData<NotificationsDataSource> getGroupsInterestsDataSource() {
        return notificationsDataSourceMutableLiveData;
    }

}
