package com.divercity.android.repository.paginated

import com.divercity.android.core.base.datasource.BaseDataSourceRepository
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase
import com.divercity.android.features.chat.usecase.FetchUsersByCharacterUseCase
import javax.inject.Inject

/**
 * Created by lucas on 01/10/2018.
 */

class UsersByCharacterPaginatedRepository @Inject
internal constructor(private val fetchUsersByCharacterUseCase: FetchUsersByCharacterUseCase) :
    BaseDataSourceRepository<Any>() {

    override fun getUseCase(): UseCase<List<Any>, Params> = fetchUsersByCharacterUseCase
}
