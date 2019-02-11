package com.divercity.android.features.onboarding.selectschool;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.android.core.base.BaseViewModel;
import com.divercity.android.core.ui.NetworkState;
import com.divercity.android.core.utils.Listing;
import com.divercity.android.data.Resource;
import com.divercity.android.data.entity.user.response.UserResponse;
import com.divercity.android.data.entity.profile.profile.User;
import com.divercity.android.data.entity.school.SchoolResponse;
import com.divercity.android.data.networking.config.DisposableObserverWrapper;
import com.divercity.android.features.onboarding.selectschool.school.SchoolPaginatedRepositoryImpl;
import com.divercity.android.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.android.repository.session.SessionRepository;
import com.divercity.android.repository.user.UserRepository;
import com.google.gson.JsonElement;

import javax.inject.Inject;

/**
 * Created by lucas on 17/10/2018.
 */

public class SelectSchoolViewModel extends BaseViewModel {

    private LiveData<PagedList<SchoolResponse>> pagedSchoolList;
    private Listing<SchoolResponse> listingPaginatedSchool;
    private SchoolPaginatedRepositoryImpl repository;
    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    private MutableLiveData<Resource<UserResponse>> updateUserProfileResponse = new MutableLiveData<>();
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
        DisposableObserverWrapper callback = new DisposableObserverWrapper<UserResponse>() {
            @Override
            protected void onFail(String error) {
                updateUserProfileResponse.postValue(Resource.Companion.error(error,null));
            }

            @Override
            protected void onHttpException(JsonElement error) {
                updateUserProfileResponse.postValue(Resource.Companion.error(error.toString(),null));
            }

            @Override
            protected void onSuccess(UserResponse o) {
                updateUserProfileResponse.postValue(Resource.Companion.success(o));
            }
        };
        User user = new User();
        user.setSchoolId(schoolResponse.getId());
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }

    public String getAccountType(){
        return sessionRepository.getAccountType();
    }

    public MutableLiveData<Resource<UserResponse>> getUpdateUserProfileResponse() {
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
