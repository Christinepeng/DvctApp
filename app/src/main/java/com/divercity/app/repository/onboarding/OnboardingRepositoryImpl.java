package com.divercity.app.repository.onboarding;

import com.divercity.app.data.entity.base.DataArray;
import com.divercity.app.data.entity.company.CompanyResponse;
import com.divercity.app.data.entity.group.GroupResponse;
import com.divercity.app.data.entity.industry.IndustryResponse;
import com.divercity.app.data.entity.major.MajorResponse;
import com.divercity.app.data.entity.school.SchoolResponse;
import com.divercity.app.data.networking.services.OnboardingService;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by lucas on 18/10/2018.
 */

public class OnboardingRepositoryImpl implements OnboardingRepository {

    private OnboardingService service;

    @Inject
    public OnboardingRepositoryImpl(OnboardingService service) {
        this.service = service;
    }

    @Override
    public Observable<DataArray<CompanyResponse>> fetchCompanies(int page, int size, String query) {
        return service.fetchCompanies(page, size, query);
    }

    @Override
    public Observable<DataArray<IndustryResponse>> fetchIndustries(int page, int size, String query) {
        return service.fetchIndustries(page, size, query);
    }

    @Override
    public Observable<DataArray<SchoolResponse>> fetchSchool(int page, int size, String query) {
        return service.fetchSchools(page, size, query);
    }

    @Override
    public Observable<DataArray<MajorResponse>> fetchMajors(int page, int size, String query) {
        return service.fetchStudentMajors(page, size, query);
    }

    @Override
    public Observable<DataArray<GroupResponse>> fetchGroups(int page, int size, String query) {
        return service.fetchGroups(page, size, query);
    }
}
