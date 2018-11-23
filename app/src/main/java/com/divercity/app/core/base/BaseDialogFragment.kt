package com.divercity.app.core.base

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.divercity.app.core.navigation.Navigator
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by lucas on 24/10/2018.
 */

abstract class BaseDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator : Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

}