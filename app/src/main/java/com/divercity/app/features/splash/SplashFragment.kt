package com.divercity.app.features.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.divercity.app.AppConstants
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import com.divercity.app.features.dialogs.CustomOneBtnDialogFragment

/**
 * Created by lucas on 24/10/2018.
 */

class SplashFragment : BaseFragment() {

    private lateinit var viewModel: SplashViewModel

    companion object {

        fun newInstance(): SplashFragment {
            return SplashFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
        subscribeToLiveData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimer()
    }

    fun startTimer() {
        Handler().postDelayed({
            //            navigator.navigateToSignUpActivity(activity!!, "test@test.com")
            //            navigator.navigateToSelectSchoolActivity(activity!!, 50)
            if (viewModel.isUserLogged)
                viewModel.fetchCurrentUserData()
//                navigator.navigateToHomeActivity(activity!!)
            else {
                navigator.navigateToEnterEmailActivity(activity!!)
                activity!!.finish()
            }
        }, AppConstants.SPLASH_SCREEN_DELAY)
    }

    fun subscribeToLiveData() {
        viewModel.userData.observe(this, Observer { listResource ->
            when (listResource?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    hideProgress()
                    showErrorDialog()
                }
                Status.SUCCESS -> {
                    hideProgress()
                }
            }
        })

        viewModel.navigateToHome.observe(this, Observer {
            navigator.navigateToHomeActivity(activity!!)
            activity!!.finish()
        })

        viewModel.navigateToSelectUserType.observe(this, Observer {
            navigator.navigateToSelectUserTypeActivity(activity!!)
            activity!!.finish()
        })
    }

    private fun showErrorDialog() {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
                "Ups!",
                getString(R.string.error_connection),
                getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { viewModel.fetchCurrentUserData() }
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }
}