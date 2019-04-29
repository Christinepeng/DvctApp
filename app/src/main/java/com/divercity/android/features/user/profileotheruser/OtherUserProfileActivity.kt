package com.divercity.android.features.user.profileotheruser

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.model.user.User

class OtherUserProfileActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_USER_ID = "intentExtraUserId"

        fun getCallingIntent(
            context: Context?,
            userId: String?,
            user : User?
        ): Intent {
            OtherUserProfileFragment.DataHolder.data = user
            val intent = Intent(context, OtherUserProfileActivity::class.java)
            intent.putExtra(INTENT_EXTRA_USER_ID, userId)
            return intent
        }
    }

    override fun fragment(): BaseFragment {
        return OtherUserProfileFragment
            .newInstance(intent.getStringExtra(INTENT_EXTRA_USER_ID))
    }

    override fun onBackPressed() {
        if (isTaskRoot)
            navigator.navigateToHomeActivity(this)
        super.onBackPressed()
    }
}
