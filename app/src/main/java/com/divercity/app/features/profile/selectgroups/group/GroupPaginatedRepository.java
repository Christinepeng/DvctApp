package com.divercity.app.features.profile.selectgroups.group;

import android.support.annotation.Nullable;

import com.divercity.app.core.utils.Listing;
import com.divercity.app.data.entity.group.GroupResponse;

/**
 * Created by lucas on 01/10/2018.
 */

public interface GroupPaginatedRepository {

    Listing<GroupResponse> fetchGroups(@Nullable String query);

    void retry();

    void refresh();

    void clear();

}
