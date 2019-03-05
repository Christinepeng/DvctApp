package com.divercity.android.features.gender.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.features.gender.base.SelectGenderFragment
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class ToolbarGenderFragment : BaseFragment(), SelectGenderFragment.Listener {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val GENDER_PICKED = "genderPicked"

        fun newInstance(): ToolbarGenderFragment {
            return ToolbarGenderFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
            R.id.fragment_fragment_container, SelectGenderFragment.newInstance()
        ).commit()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_your_gender)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onGenderChosen(gender: String) {
        val intent = Intent()
        intent.putExtra(GENDER_PICKED, gender)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}