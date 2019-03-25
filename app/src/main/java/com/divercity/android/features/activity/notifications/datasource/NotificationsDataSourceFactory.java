package com.divercity.android.features.activity.notifications.datasource;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import com.divercity.android.data.entity.activity.notification.NotificationResponse;
import com.divercity.android.features.activity.notifications.usecase.FetchNotificationsUseCase;

public class NotificationsDataSourceFactory extends DataSource.Factory<Long, NotificationResponse> {

    private FetchNotificationsUseCase fetchNotificationsUseCase;

    private MutableLiveData<NotificationsDataSource> notificationsDataSourceMutableLiveData = new MutableLiveData<>();

    public NotificationsDataSourceFactory(FetchNotificationsUseCase fetchNotificationsUseCase) {
        this.fetchNotificationsUseCase = fetchNotificationsUseCase;
    }

    @Override
    public DataSource<Long, NotificationResponse> create() {
        NotificationsDataSource followersDataSource = new NotificationsDataSource(
                fetchNotificationsUseCase);
        notificationsDataSourceMutableLiveData.postValue(followersDataSource);
        return followersDataSource;
    }

    @NonNull
    public MutableLiveData<NotificationsDataSource> getGroupsInterestsDataSource() {
        return notificationsDataSourceMutableLiveData;
    }

}
