package com.divercity.android.features.groups.createeditgroup.step2

import com.divercity.android.core.base.viewmodel.BaseViewModel
import com.divercity.android.features.groups.usecase.JoinGroupUseCase
import javax.inject.Inject

/**
 * Created by lucas on 16/10/2018.
 */

class SuggestedPicturesViewModel @Inject
constructor(
    private val joinGroupUseCase: JoinGroupUseCase
) : BaseViewModel()
