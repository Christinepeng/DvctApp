package com.divercity.app.features.onboarding.selectschool;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;

import com.divercity.app.core.base.BaseViewModel;
import com.divercity.app.core.ui.NetworkState;
import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.Resource;
import com.divercity.app.data.entity.user.response.UserResponse;
import com.divercity.app.data.entity.profile.profile.User;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.data.networking.config.DisposableObserverWrapper;
import com.divercity.app.features.onboarding.selectschool.school.SchoolPaginatedRepositoryImpl;
import com.divercity.app.features.onboarding.usecase.UpdateUserProfileUseCase;
import com.divercity.app.repository.user.UserRepository;
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

    private MutableLiveData<Resource<UserResponse>> updateUserProfileResponse = new MutableLiveData<>();
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public SelectSchoolViewModel(SchoolPaginatedRepositoryImpl repository,
                                 UpdateUserProfileUseCase updateUserProfileUseCase,
                                 UserRepository userRepository) {
        this.repository = repository;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
        this.userRepository = userRepository;
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
        getCompositeDisposable().add(callback);
        User user = new User();
        user.setSchoolId(schoolResponse.getId());
        updateUserProfileUseCase.execute(callback,UpdateUserProfileUseCase.Params.forUser(user));
    }

    public String getAccountType(){
        return userRepository.getAccountType();
    }

    public MutableLiveData<Resource<UserResponse>> getUpdateUserProfileResponse() {
        return updateUserProfileResponse;
    }

    public LiveData<PagedList<SchoolResponse>> getPagedSchoolList() {
        return pagedSchoolList;
    }
}
