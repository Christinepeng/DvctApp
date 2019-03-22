package com.divercity.android.features.groups.onboarding.datasource;

import com.divercity.android.data.entity.group.group.GroupResponse;
import com.divercity.android.features.groups.usecase.FetchGroupsUseCase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import io.reactivex.disposables.CompositeDisposable;

public class GroupDataSourceFactory extends DataSource.Factory<Long, GroupResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchGroupsUseCase fetchGroupsUseCase;
    private String query;

    private MutableLiveData<GroupDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public GroupDataSourceFactory(CompositeDisposable compositeDisposable,
                                  FetchGroupsUseCase fetchGroupsUseCase,
                                  @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchGroupsUseCase = fetchGroupsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, GroupResponse> create() {
        GroupDataSource groupDataSource = new GroupDataSource(
                compositeDisposable,
                fetchGroupsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(groupDataSource);
        return groupDataSource;
    }

    @NonNull
    public MutableLiveData<GroupDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
