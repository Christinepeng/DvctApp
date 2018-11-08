package com.divercity.app.repository.data;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.location.LocationResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public interface DataRepository {

    Observable<DataArray<CompanyResponse>> fetchCompanies(int page, int size, String query);

    Observable<DataArray<IndustryResponse>> fetchIndustries(int page, int size, String query);

    Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query);

    Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query);

    Observable<DataArray<GroupResponse>> fetchGroups(int page, int size, String query);

    Observable<DataArray<GroupResponse>> fetchFollowedGroups(int page, int size, String query);

    Observable<DataArray<LocationResponse>> fetchLocations(int page, int size, String query);
}
