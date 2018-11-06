package com.divercity.app.features.location

import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.entity.location.LocationResponse
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */
 
class ToolbarLocationFragment : BaseFragment(), SelectLocationFragment.Listener{

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        private const val PARAM_CALLED_FROM = "paramCalledFrom"

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

        (activity as SelectLocationActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.job_board)
                it.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    override fun onLocationChoosen(location: LocationResponse) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}