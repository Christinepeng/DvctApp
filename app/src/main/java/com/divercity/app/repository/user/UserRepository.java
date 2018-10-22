package com.divercity.app.repository.user;

import com.divercity.app.data.entity.base.Data;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.login.response.LoginResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public interface UserRepository {

    Observable<LoginResponse> fetchUserData(String userId);

    String getCurrentUserId();

    Observable<Data<CompanyResponse>> fetchCompanies(int page, int size, String query);

    Observable<Data<IndustryResponse>> fetchIndustries(int page, int size, String query);

    Observable<Data<SchoolResponse>> fetchSchool(int page, int size, String query);

    Observable<Data<MajorResponse>> fetchMajors(int page, int size, String query);
}
