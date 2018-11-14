package com.divercity.app.features.groups

import com.divercity.app.core.base.BaseViewModel
import com.divercity.app.features.onboarding.selectgroups.group.GroupPaginatedRepositoryImpl
import com.divercity.app.features.onboarding.selectgroups.usecase.JoinGroupUseCase
import com.divercity.app.repository.user.UserRepository
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class TabGroupsViewModel @Inject
constructor(private val repository: GroupPaginatedRepositoryImpl,
            private val userRepository: UserRepository,
            private val joinGroupUseCase: JoinGroupUseCase) : BaseViewModel() {

}
