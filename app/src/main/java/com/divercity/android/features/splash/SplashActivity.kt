package com.divercity.android.features.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SplashActivity : BaseActivity() {

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//
//    private lateinit var viewModel: SplashViewModel
//
//    @Inject
//    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun fragment(): BaseFragment = SplashFragment.newInstance()

    companion object {

        fun getCallingIntent(
            context: Context,
            bundle: Bundle
        ): Intent {
            val intent = Intent(context, SplashActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }

        fun getCallingIntent(context: Context?): Intent {
            return Intent(context, SplashActivity::class.java)
        }
    }

//    public override fun onNewIntent(intent: Intent) {
//        this.intent = intent
//        super.onNewIntent(intent)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        initBranch()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
//        subscribeToLiveData()
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
//    }
//
//    private fun initBranch() {
////      Deep link routing
//        Branch.getInstance().initSession({ referringParams, error ->
//            if (error == null) {
//                Timber.e("BRANCH SDK $referringParams")
//
//                if (referringParams.optBoolean("+clicked_branch_link", false)) {
//                    viewModel.checkRouteDeepLink(referringParams)
//                } else {
//                    /* This is because as branch.io requirement is singleTask for entry activity,
//                    *  when you open the app tapping the app icon it will always go through
//                     * this flow*/
//                    if (isTaskRoot) {
//                        viewModel.checkRouteNoDeepLink()
//                    } else {
//                        finish()
//                    }
//                }
//            } else {
//                Timber.e("BRANCH SDK ${error.message}")
//                viewModel.showBranchIOErrorDialog.call()
//            }
//        }, intent?.data, this)
//    }
//
//    private fun subscribeToLiveData() {
//        viewModel.fetchUserDataResponse.observe(this, Observer { listResource ->
//            if (Status.ERROR == listResource?.status)
//                showErrorDialog(
//                    listResource.message ?: "Error",
//                    ::fetchCurrentUserDataToCheckUserTypeDefined
//                )
//        })
//
//        viewModel.navigateToHome.observe(this, Observer {
//            navigator.navigateToHomeActivity(this)
////            navigator.navigateToSelectUserTypeActivity(this)
////            navigator.navigateToSelectGroupActivity(this, 25)
////            navigator.navigateToSelectCompanyActivity(this, 20)
//            finish()
//        })
//
//        viewModel.showBranchIOErrorDialog.observe(this, Observer {
//            showErrorDialog(
//                "Network error",
//                ::initBranch
//            )
//        })
//
//        viewModel.navigateToSelectUserType.observe(this, Observer {
//            navigator.navigateToSelectUserTypeActivity(this)
//            finish()
//        })
//
//        viewModel.navigateToEnterEmail.observe(this, Observer {
//            navigator.navigateToEnterEmailActivity(this)
//            finish()
//        })
//
//        viewModel.navigateToGroupDetail.observe(this, Observer {
//            navigator.navigateToGroupDetail(this, it.toString())
//            finish()
//        })
//
//        viewModel.navigateToResetPassword.observe(this, Observer {
//            navigator.navigateToResetPassword(this, it)
//            finish()
//        })
//    }
//
//    private fun showErrorDialog(msg: String, action: () -> Unit) {
//        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
//            "Ups!",
//            msg,
//            getString(R.string.retry)
//        )
//        customOneBtnDialogFragment.setListener { action() }
//        customOneBtnDialogFragment.isCancelable = false
//        customOneBtnDialogFragment.show(supportFragmentManager, null)
//    }
//
//    private fun fetchCurrentUserDataToCheckUserTypeDefined() {
//        viewModel.fetchCurrentUserDataToCheckUserTypeDefined()
//    }
}
