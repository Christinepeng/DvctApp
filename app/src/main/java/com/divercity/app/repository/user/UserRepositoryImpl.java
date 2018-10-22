package com.divercity.app.repository.user;

import com.divercity.app.data.entity.base.Data;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.data.networking.services.UserService;
import com.divercity.app.sharedpreference.UserSharedPreferencesRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

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
        return userService.fetchUserData(userId).doOnNext(response -> {
            if(response != null){
                userSharedPreferencesRepository.setAvatarUrl(response.getData().getAttributes().getAvatarThumb());
                userSharedPreferencesRepository.setUserId(response.getData().getId());
                userSharedPreferencesRepository.setAccountType(response.getData().getAttributes().getAccountType());
            }
        });
    }

    public String getCurrentUserId(){
        return userSharedPreferencesRepository.getUserId();
    }

    @Override
    public Observable<Data<CompanyResponse>> fetchCompanies(int page, int size, String query) {
        return userService.fetchCompanies(page, size, query);
    }

    @Override
    public Observable<Data<IndustryResponse>> fetchIndustries(int page, int size, String query) {
        return userService.fetchIndustries(page, size, query);
    }

    @Override
    public Observable<Data<SchoolResponse>> fetchSchool(int page, int size, String query) {
        return userService.fetchSchools(page, size, query);
    }

    @Override
    public Observable<Data<MajorResponse>> fetchMajors(int page, int size, String query) {
        return userService.fetchStudentMajors(page, size, query);
    }
}
