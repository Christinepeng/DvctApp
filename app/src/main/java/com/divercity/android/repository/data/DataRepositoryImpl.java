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
import com.divercity.android.data.networking.services.DataService;

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
    public Observable<DataArray<LocationResponse>> fetchLocations(int page, int size, String query) {
        return service.fetchLocations(page, size, query).map(response -> {
            checkResponse(response);
            return response.body();
        });
    }

    @Override
    public Observable<List<SkillResponse>> fetchSkills(int page, int size, String query) {
        return service.fetchSkills(page, size, query).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @Override
    public Observable<Boolean> createCompany(CreateCompanyBody body) {
        return service.createCompany(body).map(response -> {
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

    @Override
    public Observable<List<InterestsResponse>> fetchInterests() {
        return service.fetchInterests().map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    @Override
    public Observable<List<OOIResponse>> fetchOccupationOfInterests(int pageNumber, int size, String query) {
        return service.fetchOccupationOfInterests(pageNumber, size, query).map(response -> {
            checkResponse(response);
            return response.body().getData();
        });
    }

    private void checkResponse(Response response) {
        if (!response.isSuccessful())
            throw new HttpException(response);
    }
}
