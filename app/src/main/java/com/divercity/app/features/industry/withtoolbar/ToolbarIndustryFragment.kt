package com.divercity.app.features.industry.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.industry.IndustryResponse
import com.divercity.app.features.industry.base.SelectIndustryFragment
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class ToolbarIndustryFragment : BaseFragment(), SelectIndustryFragment.Listener {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val INDUSTRY_PICKED = "locationPicked"

        fun newInstance(): ToolbarIndustryFragment {
            return ToolbarIndustryFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectIndustryFragment.newInstance()).commit()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_industry)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onIndustryChosen(industry: IndustryResponse) {
        val intent = Intent()
        intent.putExtra(INDUSTRY_PICKED, industry)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}