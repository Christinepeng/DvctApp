package com.divercity.app.features.profile.selectindustry.industry;

import android.support.annotation.Nullable;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.industry.IndustryResponse;

/**
 * Created by lucas on 01/10/2018.
 */

public interface IndustryPaginatedRepository {

    Listing<IndustryResponse> fetchCompanies(@Nullable String query);

    void retry();

    void refresh();

    void clear();

}
