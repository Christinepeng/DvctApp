package com.divercity.android.features.jobs.jobposting.skills.datasource;

import com.divercity.android.data.entity.skills.SkillResponse;
import com.divercity.android.features.skill.base.usecase.FetchSkillsUseCase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
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
