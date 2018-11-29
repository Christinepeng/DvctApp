package com.divercity.app.repository.data;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.base.DataObject;
import com.divercity.app.data.entity.company.response.CompanyResponse;
import com.divercity.app.data.entity.company.createcompanybody.CreateCompanyBody;
import com.divercity.app.data.entity.company.sizes.CompanySizeResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.group.recommendedgroups.RecommendedGroupsResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.location.LocationResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.data.entity.skills.SkillResponse;
import com.divercity.app.data.networking.services.DataService;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by lucas on 18/10/2018.
 */

public class DataRepositoryImpl implements DataRepository {

    private DataService service;

    @Inject
    public DataRepositoryImpl(DataService service) {
        this.service = service;
    }

    @Override
    public Observable<List<CompanyResponse>> fetchCompanies(int page, int size, String query) {
        return service.fetchCompanies(page, size, query).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @Override
    public Observable<DataArray<IndustryResponse>> fetchIndustries(int page, int size, String query) {
        return service.fetchIndustries(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query) {
        return service.fetchSchools(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query) {
        return service.fetchStudentMajors(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<GroupResponse>> fetchGroups(int page, int size, String query) {
        return service.fetchGroups(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataObject<RecommendedGroupsResponse>> fetchRecommendedGroups() {
        return service.fetchRecommendedGroups().map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<GroupResponse>> fetchFollowedGroups(int page, int size, String query) {
        return service.fetchFollowedGroups(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<LocationResponse>> fetchLocations(int page, int size, String query) {
        return service.fetchLocations(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<SkillResponse>> fetchSkills(int page, int size, String query) {
        return service.fetchSkills(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<GroupResponse>> fetchTrendingGroups(int page, int size, String query) {
        return service.fetchTrendingGroups(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<DataArray<GroupResponse>> fetchMyGroups(int page, int size, String query) {
        return service.fetchMyGroups(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<Boolean> createCompany(String userId, CreateCompanyBody body) {
        return service.createCompany(userId, body).map(response -> {
            checkResponse(response);
            return true;
        });
    }

    @Override
    public Observable<List<CompanySizeResponse>> fetchCompanySizes() {
        return service.fetchCompanySizes().map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    private void checkResponse(Response response) {
        if (!response.isSuccessful())
            throw new HttpException(response);
    }
}
