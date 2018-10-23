package com.divercity.app.repository.user;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.data.entity.profile.UserProfileBody;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.data.networking.services.UserService;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by lucas on 18/10/2018.
 */

public class UserRepositoryImpl implements UserRepository {

    private UserService userService;
    private UserSharedPreferencesRepository userSharedPreferencesRepository;

    @Inject
    public UserRepositoryImpl(UserService userService,
                              UserSharedPreferencesRepository userSharedPreferencesRepository) {
        this.userService = userService;
        this.userSharedPreferencesRepository = userSharedPreferencesRepository;
    }

    @Override
    public Observable<LoginResponse> fetchUserData(String userId) {
        return userService.fetchUserData(userId)
                .map(loginResponseDataObject -> loginResponseDataObject.getData())
                .doOnNext(response -> {
                    if (response != null) {
                        userSharedPreferencesRepository.setAvatarUrl(response.getAttributes().getAvatarThumb());
                        userSharedPreferencesRepository.setUserId(response.getId());
                        userSharedPreferencesRepository.setAccountType(response.getAttributes().getAccountType());
                    }
                });
    }

    public String getCurrentUserId() {
        return userSharedPreferencesRepository.getUserId();
    }

    @Override
    public String getCurrentUserAccountType() {
        return userSharedPreferencesRepository.getAccountType();
    }

    @Override
    public Observable<DataArray<CompanyResponse>> fetchCompanies(int page, int size, String query) {
        return userService.fetchCompanies(page, size, query);
    }

    @Override
    public Observable<DataArray<IndustryResponse>> fetchIndustries(int page, int size, String query) {
        return userService.fetchIndustries(page, size, query);
    }

    @Override
    public Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query) {
        return userService.fetchSchools(page, size, query);
    }

    @Override
    public Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query) {
        return userService.fetchStudentMajors(page, size, query);
    }

    @Override
    public Observable<DataArray<GroupResponse>> fetchGroups(int page, int size, String query) {
        return userService.fetchGroups(page, size, query);
    }

    @Override
    public Observable<Boolean> joinGroup(String idGroup) {
        return userService.joinGroup(idGroup).map(Response::isSuccessful);
    }

    @Override
    public Observable<LoginResponse> updateUserProfile(User user) {
        UserProfileBody userProfileBody = new UserProfileBody();
        userProfileBody.setUser(user);
        return userService.updateUserProfile(
                userSharedPreferencesRepository.getUserId(),
                userProfileBody).map(response -> {
                    if (response != null) {
                        userSharedPreferencesRepository.setAvatarUrl(response.getData().getAttributes().getAvatarThumb());
                        userSharedPreferencesRepository.setUserId(response.getData().getId());
                        userSharedPreferencesRepository.setAccountType(response.getData().getAttributes().getAccountType());
                    }
                    return response.getData();
                }
        );
    }
}
