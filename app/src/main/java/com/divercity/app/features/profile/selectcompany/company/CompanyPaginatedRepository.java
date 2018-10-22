package com.divercity.app.features.profile.selectcompany.company;

import android.support.annotation.Nullable;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.company.CompanyResponse;

/**
 * Created by lucas on 01/10/2018.
 */

public interface CompanyPaginatedRepository {

    Listing<CompanyResponse> fetchCompanies(@Nullable String query);

    void retry();

    void refresh();

    void clear();

}
