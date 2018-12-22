package com.divercity.app.features.agerange.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.ui.IOnBackPressed
import com.divercity.app.features.agerange.base.SelectAgeFragment
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class ToolbarAgeFragment : BaseFragment(), SelectAgeFragment.Listener, IOnBackPressed {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    var ageRangeSelected: String? = null

    companion object {
        const val AGE_RANGE_PICKED = "ageRangePicked"

        fun newInstance(): ToolbarAgeFragment {
            return ToolbarAgeFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectAgeFragment.newInstance()).commit()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_your_agerange)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onAgeRangeChosen(ageRange: String) {
        ageRangeSelected = ageRange
    }

    override fun onBackPressed(): Boolean {
        val intent = Intent()
        intent.putExtra(AGE_RANGE_PICKED, ageRangeSelected)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
        }
        return true
    }
}