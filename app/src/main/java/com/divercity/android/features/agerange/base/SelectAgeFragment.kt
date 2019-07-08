package com.divercity.android.features.agerange.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list_search.*
import javax.inject.Inject

class SelectAgeFragment : BaseFragment() {

    @Inject
    lateinit var adapter: SelectAgeAdapter

    var fragListener: Listener? = null

    companion object {

        fun newInstance(): SelectAgeFragment {
            return SelectAgeFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_search

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragListener = parentFragment as Listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        include_search.visibility = View.GONE
        lay_action.visibility = View.GONE

        adapter.setListener(listener)
        list.adapter = adapter
    }

    private val listener: SelectAgeAdapter.Listener = SelectAgeAdapter.Listener {
        fragListener?.onAgeRangeChosen(it)
    }

    interface Listener {

        fun onAgeRangeChosen(ageRange: String)
    }
}
