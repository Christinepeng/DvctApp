package com.divercity.android.features.major.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.major.base.SelectMajorFragment
import com.divercity.android.model.Major
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class SelectSingleMajorFragment : BaseFragment(), SelectMajorFragment.Listener {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val MAJOR_PICKED = "majorPicked"

        fun newInstance(): SelectSingleMajorFragment {
            return SelectSingleMajorFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction()
            .add(
                R.id.fragment_fragment_container,
                SelectMajorFragment.newInstance()
            ).commit()

        (activity as SelectSingleMajorActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_major)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onMajorChosen(major: Major) {
        val intent = Intent()
        intent.putExtra(MAJOR_PICKED, major)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}