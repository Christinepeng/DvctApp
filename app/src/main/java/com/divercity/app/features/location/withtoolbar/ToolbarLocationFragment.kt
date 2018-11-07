package com.divercity.app.features.location.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.location.LocationResponse
import com.divercity.app.features.location.base.SelectLocationFragment
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */
 
class ToolbarLocationFragment : BaseFragment(), SelectLocationFragment.Listener{

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        private const val PARAM_CALLED_FROM = "paramCalledFrom"
        const val LOCATION_PICKED = "locationPicked"

        fun newInstance(calledFrom: String): ToolbarLocationFragment {
            val fragment = ToolbarLocationFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_CALLED_FROM, calledFrom)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectLocationFragment.newInstance()).commit()

        (activity as ToolbarLocationActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_location)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onLocationChoosen(location: LocationResponse) {
        val intent = Intent()
        intent.putExtra(LOCATION_PICKED, location)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}