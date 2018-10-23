package com.divercity.app.repository.user;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.profile.User;
import com.divercity.app.data.entity.school.SchoolResponse;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public interface UserRepository {

    Observable<LoginResponse> fetchUserData(String userId);

    Observable<LoginResponse> updateUserProfile(User user);

    String getCurrentUserId();

    String getCurrentUserAccountType();

    Observable<DataArray<CompanyResponse>> fetchCompanies(int page, int size, String query);

    Observable<DataArray<IndustryResponse>> fetchIndustries(int page, int size, String query);

    Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query);

    Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query);

    Observable<DataArray<GroupResponse>> fetchGroups(int page, int size, String query);

    Observable<Boolean> joinGroup(String idGroup);
}
