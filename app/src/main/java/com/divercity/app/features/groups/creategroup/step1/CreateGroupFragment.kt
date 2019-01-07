package com.divercity.app.features.groups.creategroup.step1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_group_detail.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 25/10/2018.
 */


class CreateGroupFragment : BaseFragment() {

    lateinit var viewModel : CreateGroupViewModel

    companion object {

        fun newInstance(): CreateGroupFragment {
            return CreateGroupFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_create_group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.create_group)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }
}