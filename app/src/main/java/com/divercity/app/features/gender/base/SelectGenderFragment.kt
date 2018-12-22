package com.divercity.app.features.gender.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_list_search.*
import javax.inject.Inject

/**
 * Created by lucas on 25/10/2018.
 */


class SelectGenderFragment : BaseFragment() {

    @Inject
    lateinit var adapter: SelectGenderAdapter

    var fragListener: Listener? = null

    companion object {

        fun newInstance(): SelectGenderFragment {
            return SelectGenderFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_list_search

    override fun onAttach(context: Context?) {
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

    private val listener: SelectGenderAdapter.Listener = SelectGenderAdapter.Listener {
        fragListener?.onGenderChosen(getString(it.textId))
    }

    interface Listener {

        fun onGenderChosen(gender: String)
    }
}