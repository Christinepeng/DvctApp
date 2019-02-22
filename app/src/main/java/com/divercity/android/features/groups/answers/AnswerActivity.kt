package com.divercity.android.features.groups.answers

import android.content.Context
import android.content.Intent
import com.divercity.android.core.base.BaseActivity
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.groups.answers.model.Question

/**
 * Created by lucas on 24/12/2018.
 */

class AnswerActivity : BaseActivity() {

    companion object {
        private const val INTENT_EXTRA_PARAM_QUESTION = "question"

        fun getCallingIntent(context: Context?, question: Question): Intent {
            val intent = Intent(context, AnswerActivity::class.java)
            intent.putExtra(INTENT_EXTRA_PARAM_QUESTION, question)
            return intent
        }
    }

    override fun fragment(): BaseFragment = AnswerFragment.newInstance(
        intent.getParcelableExtra(INTENT_EXTRA_PARAM_QUESTION))
}