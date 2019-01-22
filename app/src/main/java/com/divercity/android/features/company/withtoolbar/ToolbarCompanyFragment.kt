package com.divercity.android.features.company.withtoolbar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.base.SelectCompanyFragment
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.view_toolbar.view.*

/**
 * Created by lucas on 06/11/2018.
 */

class ToolbarCompanyFragment : BaseFragment(), SelectCompanyFragment.Listener {

    override fun layoutId(): Int = R.layout.fragment_toolbar

    companion object {
        const val COMPANY_PICKED = "locationPicked"

        fun newInstance(): ToolbarCompanyFragment {
            return ToolbarCompanyFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.beginTransaction().add(
                R.id.fragment_fragment_container, SelectCompanyFragment.newInstance()).commit()

        (activity as ToolbarCompanyActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_company)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onCompanyChosen(company: CompanyResponse) {
        val intent = Intent()
        intent.putExtra(COMPANY_PICKED, company)
        activity?.apply {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}