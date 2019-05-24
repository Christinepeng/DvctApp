package com.divercity.android.features.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.navigation.Navigator
import com.divercity.android.data.Status
import com.divercity.android.features.dialogs.CustomOneBtnDialogFragment
import dagger.android.support.DaggerAppCompatActivity
import io.branch.referral.Branch
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SplashViewModel

    companion object {

//        const val PARAM_PN_TYPE = "paramPushNotificationType"
//
//        // ADD CHAT MESSAGE
//        const val TYPE_PN_NEW_MESSAGE = 1
//
//        fun getChatMessagePNBundle(
//            pnType: String,
//            userName: String,
//            userId: String?,
//            chatId: Int
//        ): Bundle {
//            val bundle = Bundle()
//            bundle.putString(PARAM_PN_TYPE, pnType)
//            bundle.putString(ChatActivity.PARAM_USER_ID, userId)
//            bundle.putString(ChatActivity.PARAM_DISPLAY_NAME, userName)
//            bundle.putInt(ChatActivity.PARAM_CHAT_ID, chatId)
//            return bundle
//        }

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

    public override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }

    override fun onStart() {
        super.onStart()
        initBranch()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]
        subscribeToLiveData()
    }

    private fun initBranch() {
//      Deep link routing
        Branch.getInstance().initSession({ referringParams, error ->
            if (error == null) {
                Timber.e("BRANCH SDK $referringParams")

                if (referringParams.optBoolean("+clicked_branch_link", false)) {
                    viewModel.checkRouteDeepLink(referringParams)
                } else {
                    /* This is because as branch.io requirement is singleTask for entry activity,
                    *  when you open the app tapping the app icon it will always go through
                     * this flow*/
                    if (isTaskRoot) {
                        viewModel.checkRouteNoDeepLink()
                    } else {
                        finish()
                    }
                }
            } else {
                Timber.e("BRANCH SDK ${error.message}")
                viewModel.showBranchIOErrorDialog.call()
            }
        }, intent?.data, this)
    }

    private fun subscribeToLiveData() {
        viewModel.fetchUserDataResponse.observe(this, Observer { listResource ->
            if (Status.ERROR == listResource?.status)
                showErrorDialog(
                    listResource.message ?: "Error",
                    ::fetchCurrentUserDataToCheckUserTypeDefined
                )
        })

        viewModel.navigateToHome.observe(this, Observer {
            navigator.navigateToHomeActivity(this)
//            navigator.navigateToSelectGroupActivity(this, 25)
//            navigator.navigateToSelectCompanyActivity(this, 20)
            finish()
        })

        viewModel.showBranchIOErrorDialog.observe(this, Observer {
            showErrorDialog(
                "Network error",
                ::initBranch
            )
        })

        viewModel.navigateToSelectUserType.observe(this, Observer {
            navigator.navigateToSelectUserTypeActivity(this)
            finish()
        })

        viewModel.navigateToEnterEmail.observe(this, Observer {
            navigator.navigateToEnterEmailActivity(this)
            finish()
        })

        viewModel.navigateToGroupDetail.observe(this, Observer {
            navigator.navigateToGroupDetail(this, it.toString())
            finish()
        })

        viewModel.navigateToResetPassword.observe(this, Observer {
            navigator.navigateToResetPassword(this, it)
            finish()
        })
    }

    private fun showErrorDialog(msg: String, action: () -> Unit) {
        val customOneBtnDialogFragment = CustomOneBtnDialogFragment.newInstance(
            "Ups!",
            msg,
            getString(R.string.retry)
        )
        customOneBtnDialogFragment.setListener { action() }
        customOneBtnDialogFragment.isCancelable = false
        customOneBtnDialogFragment.show(supportFragmentManager, null)
    }

    private fun fetchCurrentUserDataToCheckUserTypeDefined() {
        viewModel.fetchCurrentUserDataToCheckUserTypeDefined()
    }
}
