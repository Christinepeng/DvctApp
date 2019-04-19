package com.divercity.android.features.company.companydetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.dialogs.CompanyActionsDialogFragment
import com.divercity.android.features.dialogs.CustomTwoBtnDialogFragment
import kotlinx.android.synthetic.main.fragment_company_detail.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class CompanyDetailFragment : BaseFragment(), CompanyActionsDialogFragment.Listener {

    lateinit var viewModel: CompanyDetailViewModel

    @Inject
    lateinit var adapter: CompanyDetailViewPagerAdapter

    var company: CompanyResponse? = null
    lateinit var companyId: String

    companion object {
        private const val PARAM_COMPANY_ID = "paramCompanyId"

        fun newInstance(companyId: String): CompanyDetailFragment {
            val fragment = CompanyDetailFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_COMPANY_ID, companyId)
            fragment.arguments = arguments
            return fragment
        }
    }

    enum class DataHolder {
        INSTANCE;

        private var company: CompanyResponse? = null

        companion object {

            fun hasData(): Boolean {
                return INSTANCE.company != null
            }

            var data: CompanyResponse?
                get() {
                    val company = INSTANCE.company
                    INSTANCE.company = null
                    return company
                }
                set(objectList) {
                    INSTANCE.company = objectList
                }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_company_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        companyId = arguments?.getString(PARAM_COMPANY_ID)!!
        if (DataHolder.hasData()) {
            company = DataHolder.data
            showData(company)
        } else {
            fetchJobData()
        }
        setupToolbar()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompanyDetailViewModel::class.java)
    }

    private fun fetchJobData() {
        viewModel.fetchJobById(companyId)
    }

    private fun showDialogMoreActions() {
        if (company != null) {
            val dialog =
                CompanyActionsDialogFragment
                    .newInstance(company?.attributes?.currentUserAdmin ?: false)
            dialog.show(childFragmentManager, null)
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.jobs)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        btn_toolbar_more.setOnClickListener {
            showDialogMoreActions()
        }
    }

    private fun showData(company: CompanyResponse?) {
        company?.also {
            adapter.company = it
            viewpager.adapter = adapter
            tab_layout.setupWithViewPager(viewpager)

            root_layout.visibility = View.VISIBLE

            GlideApp.with(this)
                .load(it.attributes?.photos?.medium)
                .into(img)

            txt_name.text = it.attributes?.name

            if (it.attributes?.industry == null && it.attributes?.headquarters == null) {
                txt_subtitle1.visibility = View.GONE
            } else {
                txt_subtitle1.visibility = View.VISIBLE

                var subtitle = ""
                if (it.attributes.industry != null)
                    subtitle = it.attributes.industry

                if (it.attributes.industry != null && it.attributes.headquarters != null)
                    subtitle.plus(" · ")

                if (it.attributes.headquarters != null)
                    subtitle.plus(it.attributes.headquarters)

                txt_subtitle1.text = subtitle
            }

            if (it.attributes?.companySize == null)
                txt_size.visibility = View.GONE
            else
                txt_size.text = it.attributes.companySize
        }
    }

    private fun showDialogConnectionError(jobId: String) {
        val dialog = CustomTwoBtnDialogFragment.newInstance(
            getString(R.string.ups),
            getString(R.string.error_connection),
            getString(R.string.cancel),
            getString(R.string.retry)
        )

        dialog.setListener(object : CustomTwoBtnDialogFragment.OnBtnListener {

            override fun onNegativeBtnClick() {
                viewModel.fetchJobById(jobId)
            }

            override fun onPositiveBtnClick() {
                activity!!.finish()
            }
        })
        dialog.isCancelable = false
        dialog.show(childFragmentManager, null)
    }


    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    override fun onShareJobViaMessage() {

    }

    override fun onShareJobToGroups() {

    }

    override fun onReportCompany() {

    }

    override fun onEditAdmins() {
        navigator.navigateToDeleteCompanyAdmin(
            this@CompanyDetailFragment,
            companyId,
            ""
        )
    }

    override fun onAddAdmins() {
        navigator.navigateToCompanyAdmins(this@CompanyDetailFragment, companyId)
    }
}