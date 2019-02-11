package com.divercity.android.features.ethnicity.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.ethnicity.base.Ethnicity
import com.divercity.android.features.ethnicity.base.SelectEthnicityFragment
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class ToolbarEthnicityFragment : BaseFragment(), SelectEthnicityFragment.Listener {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val ETHNICITY_PICKED = "ethnicityPicked"

        fun newInstance(): ToolbarEthnicityFragment {
            return ToolbarEthnicityFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectEthnicityFragment.newInstance()).commit()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_your_ethnicity)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onEthnicityChosen(ethnicity: Ethnicity) {
        val intent = Intent()
        intent.putExtra(ETHNICITY_PICKED, getString(ethnicity.textId))
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}