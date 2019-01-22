package com.divercity.android.features.jobposting.skills.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.divercity.android.data.entity.skills.SkillResponse;
import com.divercity.android.features.jobposting.skills.usecase.FetchSkillsUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class SkillDataSourceFactory extends DataSource.Factory<Long, SkillResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchSkillsUseCase fetchSkillsUseCase;
    private String query;

    private MutableLiveData<SkillDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public SkillDataSourceFactory(CompositeDisposable compositeDisposable,
                                  FetchSkillsUseCase fetchSkillsUseCase,
                                  @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchSkillsUseCase = fetchSkillsUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, SkillResponse> create() {
        SkillDataSource skillDataSource = new SkillDataSource(
                compositeDisposable,
                fetchSkillsUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(skillDataSource);
        return skillDataSource;
    }

    @NonNull
    public MutableLiveData<SkillDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
