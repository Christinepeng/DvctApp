package com.divercity.app.repository.data;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.company.createcompanybody.CreateCompanyBody;
import com.divercity.app.data.entity.company.response.CompanyResponse;
import com.divercity.app.data.entity.company.sizes.CompanySizeResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.group.recommendedgroups.RecommendedGroupsResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.location.LocationResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.data.entity.skills.SkillResponse;
import io.reactivex.Observable;

import java.util.List;

/**
 * Created by lucas on 18/10/2018.
 */

public interface DataRepository {

    Observable<List<CompanyResponse>> fetchCompanies(int page, int size, String query);

    Observable<DataArray<IndustryResponse>> fetchIndustries(int page, int size, String query);

    Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query);

    Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query);

    Observable<DataArray<GroupResponse>> fetchGroups(int page, int size, String query);

    Observable<DataObject<RecommendedGroupsResponse>>fetchRecommendedGroups();

    Observable<DataArray<GroupResponse>> fetchFollowedGroups(int page, int size, String query);

    Observable<DataArray<LocationResponse>> fetchLocations(int page, int size, String query);

    Observable<DataArray<SkillResponse>> fetchSkills(int page, int size, String query);

    Observable<DataArray<GroupResponse>> fetchTrendingGroups(int page, int size, String query);

    Observable<DataArray<GroupResponse>> fetchMyGroups(int page, int size, String query);

    Observable<Boolean> createCompany(CreateCompanyBody body);

    Observable<List<CompanySizeResponse>> fetchCompanySizes();
}
