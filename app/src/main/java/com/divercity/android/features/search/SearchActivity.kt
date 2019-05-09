package com.divercity.android.features.search

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment

class SearchActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_SEARCH_QUERY = "intentExtraParamSearchQuery"

        fun getCallingIntent(
            context: Context?,
            searchQuery: String?
        ): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_SEARCH_QUERY, searchQuery)
            return intent
        }
    }

    override fun fragment(): BaseFragment = SearchFragment
        .newInstance(intent.getStringExtra(INTENT_EXTRA_PARAM_SEARCH_QUERY))
}
