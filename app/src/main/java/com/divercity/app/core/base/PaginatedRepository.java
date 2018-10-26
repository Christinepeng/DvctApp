package com.divercity.app.core.base;

import com.divercity.app.core.utils.Listing;

/**
 * Created by lucas on 01/10/2018.
 */

public interface PaginatedRepository<T> {

    Listing<T> fetchData();

    void retry();

    void refresh();

    void clear();

}
