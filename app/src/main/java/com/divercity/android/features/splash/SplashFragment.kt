package com.divercity.android.features.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.divercity.android.AppConstants
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment

/**
 * Created by lucas on 24/10/2018.
 */

class SplashFragment : BaseFragment() {

    private lateinit var viewModel: SplashViewModel
    private var handler = Handler()

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
//        startTimer()

        if (viewModel.isUserLogged)
            viewModel.fetchCurrentUserDataToCheckUserTypeDefined()
//                navigator.navigateToChatActivity(this,"Joseph Student","6")
//                navigator.navigateToSelectInterestsActivity(activity!!, 20)
//                navigator.navigateToSelectUserTypeActivity(activity!!)
//                navigator.navigateToSelectOccupationActivity(activity!!, 20)
//                navigator.navigateToOnboardingIndustryActivity(activity!!, 30)
//            navigator.navigateToSelectSingleIndustryActivityForResult(this, 30)
//            navigator.navigateToOnboardingGenderActivity(activity!!, 30)
//            navigator.navigateToOnboardingLocationActivity(activity!!, 76)
        else {
            navigator.navigateToEnterEmailActivity(activity!!)
            activity!!.finish()
        }
    }

    fun startTimer() {
        handler.postDelayed({
            //   navigator.navigateToProfilePromptActivity(activity!!)
//                        navigator.navigateToSignUpActivity(activity!!, "test@test.com")
//                        navigator.navigateToSelectSchoolActivity(activity!!, 50)
//                            navigator.navigateToShareJobGroupActivity(this,"21")
//                navigator.navigateToShareJobGroupActivity(this,"31")
//                navigator.navigateToHomeActivity(activity!!)
            //                       navigator.navigateToProfilePromptActivity(activity!!)
//                navigator.navigateToSelectUserTypeActivity(activity!!)
//                navigator.navigateToSelectUserTypeActivity(activity!!)
//                navigator.navigateToSelectGroupActivity(activity!!, 90)

//            navigator.navigateToOnboardingLocationActivity(activity!!, 40)

            if (viewModel.isUserLogged)
                viewModel.fetchCurrentUserDataToCheckUserTypeDefined()
//                navigator.navigateToChatActivity(this,"Joseph Student","6")
//                navigator.navigateToSelectInterestsActivity(activity!!, 20)
//                navigator.navigateToSelectUserTypeActivity(activity!!)
//                navigator.navigateToSelectOccupationActivity(activity!!, 20)
//                navigator.navigateToOnboardingIndustryActivity(activity!!, 30)
//            navigator.navigateToSelectSingleIndustryActivityForResult(this, 30)
//            navigator.navigateToOnboardingGenderActivity(activity!!, 30)
//            navigator.navigateToOnboardingLocationActivity(activity!!, 76)
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
                    showErrorDialog(listResource.message ?: "Error")
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

    private fun showErrorDialog(msg: String) {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
                "Ups!",
                msg,
                getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { viewModel.fetchCurrentUserDataToCheckUserTypeDefined() }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(childFragmentManager, null)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}