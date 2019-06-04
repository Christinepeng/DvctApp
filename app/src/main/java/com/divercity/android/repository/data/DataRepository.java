package com.divercity.android.repository.data;

import com.divercity.android.data.entity.base.DataArray;
import com.divercity.android.data.entity.company.createcompanybody.CreateCompanyBody;
import com.divercity.android.data.entity.company.response.CompanyResponse;
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse;
import com.divercity.android.data.entity.industry.IndustryResponse;
import com.divercity.android.data.entity.interests.InterestsResponse;
import com.divercity.android.data.entity.location.LocationResponse;
import com.divercity.android.data.entity.major.MajorResponse;
import com.divercity.android.data.entity.occupationofinterests.OOIResponse;
import com.divercity.android.data.entity.school.SchoolResponse;
import com.divercity.android.data.entity.skills.SkillResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public interface DataRepository {

    Observable<List<CompanyResponse>> fetchCompanies(int page, int size, String query);

    Observable<List<IndustryResponse>> fetchIndustries(int page, int size, String query);

    Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query);

    Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query);

    Observable<List<LocationResponse>> fetchLocations(int page, int size, String query);

    Observable<List<SkillResponse>> fetchSkills(int page, int size, String query);

    Observable<Boolean> createCompany(CreateCompanyBody body);

    Observable<List<CompanySizeResponse>> fetchCompanySizes();

    Observable<List<InterestsResponse>> fetchInterests();

    Observable<List<OOIResponse>> fetchOccupationOfInterests(
            int pageNumber,
            int size,
            String query);

}
