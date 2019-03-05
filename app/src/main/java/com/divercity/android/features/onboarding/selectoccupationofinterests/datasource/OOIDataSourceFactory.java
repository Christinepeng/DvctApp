package com.divercity.android.features.onboarding.selectoccupationofinterests.datasource;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.divercity.android.data.entity.occupationofinterests.OOIResponse;
import com.divercity.android.features.onboarding.selectoccupationofinterests.usecase.FetchOOIUseCase;

import io.reactivex.disposables.CompositeDisposable;

public class OOIDataSourceFactory extends DataSource.Factory<Long, OOIResponse> {

    private CompositeDisposable compositeDisposable;
    private FetchOOIUseCase fetchOOIUseCase;
    private String query;

    private MutableLiveData<OOIDataSource> mGroupsInterestsDataSourceMutableLiveData = new MutableLiveData<>();

    public OOIDataSourceFactory(CompositeDisposable compositeDisposable,
                                FetchOOIUseCase fetchOOIUseCase,
                                @Nullable String query) {
        this.compositeDisposable = compositeDisposable;
        this.fetchOOIUseCase = fetchOOIUseCase;
        this.query = query;
    }

    @Override
    public DataSource<Long, OOIResponse> create() {
        OOIDataSource OOIDataSource = new OOIDataSource(
                compositeDisposable,
                fetchOOIUseCase,
                query);
        mGroupsInterestsDataSourceMutableLiveData.postValue(OOIDataSource);
        return OOIDataSource;
    }

    @NonNull
    public MutableLiveData<OOIDataSource> getGroupsInterestsDataSource() {
        return mGroupsInterestsDataSourceMutableLiveData;
    }

}
