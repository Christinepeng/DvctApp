package com.divercity.android.features.onboarding.selectschool;

import com.divercity.android.core.base.viewmodel.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.profile.profile.UserProfileEntity;
import com.divercity.android.data.entity.school.SchoolResponse;
import com.divercity.android.data.entity.user.response.UserEntityResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.onboarding.selectschool.school.SchoolPaginatedRepositoryImpl;
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectSchoolViewModel extends BaseViewModel {

    private LiveData<PagedList<SchoolResponse>> pagedSchoolList;
    private Listing<SchoolResponse> listingPaginatedSchool;
    private SchoolPaginatedRepositoryImpl repository;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    private MutableLiveData<Resource<UserEntityResponse>> updateUserProfileResponse = new MutableLiveData<>();
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectSchoolViewModel(SchoolPaginatedRepositoryImpl repository,
                                 UpdateUserProfileUseCase updateUserProfileUseCase,
                                 UserRepository userRepository,
                                 SessionRepository sessionRepository) {
        this.repository = repository;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public void retry() {
        repository.retry();
    }

    public void refresh() {
        repository.refresh();
    }

    public LiveData<NetworkState> getNetworkState() {
        return listingPaginatedSchool.getNetworkState();
    }

    public LiveData<NetworkState> getRefreshState() {
        return listingPaginatedSchool.getRefreshState();
    }

    public void fetchSchools(LifecycleOwner lifecycleOwner,  @Nullable String query){
        if(pagedSchoolList != null) {
            listingPaginatedSchool.getNetworkState().removeObservers(lifecycleOwner);
            listingPaginatedSchool.getRefreshState().removeObservers(lifecycleOwner);
            pagedSchoolList.removeObservers(lifecycleOwner);
        }
        listingPaginatedSchool = repository.fetchData(query);
        pagedSchoolList = listingPaginatedSchool.getPagedList();
    }

    public void updateUserProfile(SchoolResponse schoolResponse){
        updateUserProfileResponse.postValue(Resource.Companion.loading(null));
        DisposableObserverWrapper callback = new DisposableObserverWrapper<UserEntityResponse>() {
            @Override
            protected void onFail(String error) {
                updateUserProfileResponse.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                updateUserProfileResponse.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(UserEntityResponse o) {
                updateUserProfileResponse.postValue(Resource.Companion.success(o));
            }
        };
        UserProfileEntity user = new UserProfileEntity();
        user.setSchoolId(schoolResponse.getId());
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }

    public String getAccountType(){
        return sessionRepository.getAccountType();
    }

    public MutableLiveData<Resource<UserEntityResponse>> getUpdateUserProfileResponse() {
        return updateUserProfileResponse;
    }

    public LiveData<PagedList<SchoolResponse>> getPagedSchoolList() {
        return pagedSchoolList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        updateUserProfileUseCase.dispose();
    }
}
