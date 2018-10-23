package com.divercity.app.features.profile.selectmajor.major;

import android.support.annotation.Nullable;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.major.MajorResponse;

/**
 * Created by lucas on 01/10/2018.
 */

public interface MajorPaginatedRepository {

    Listing<MajorResponse> fetchMajors(@Nullable String query);

    void retry();

    void refresh();

    void clear();

}
