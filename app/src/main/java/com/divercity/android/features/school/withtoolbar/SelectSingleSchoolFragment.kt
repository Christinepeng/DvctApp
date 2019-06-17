package com.divercity.android.features.school.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.school.base.SelectSchoolFragment
import com.divercity.android.model.School
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class SelectSingleSchoolFragment : BaseFragment(), SelectSchoolFragment.Listener {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val SCHOOL_PICKED = "schoolPicked"

        fun newInstance(): SelectSingleSchoolFragment {
            return SelectSingleSchoolFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .add(
                R.id.fragment_fragment_container,
                SelectSchoolFragment.newInstance()
            ).commit()

        (activity as SelectSingleSchoolActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_school)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onSchoolChosen(school: School) {
        val intent = Intent()
        intent.putExtra(SCHOOL_PICKED, school)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}