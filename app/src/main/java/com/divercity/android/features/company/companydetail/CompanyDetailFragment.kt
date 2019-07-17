package com.divercity.android.features.company.companydetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.dialogs.CompanyActionsDialogFragment
import kotlinx.android.synthetic.main.fragment_company_detail.*
import kotlinx.android.synthetic.main.view_company_header.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

/**
 * Created by lucas on 16/11/2018.
 */

class CompanyDetailFragment : BaseFragment(), CompanyActionsDialogFragment.Listener {

    lateinit var viewModel: CompanyDetailViewModel

    @Inject
    lateinit var adapter: CompanyDetailViewPagerAdapter

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
        viewModel.companyId = arguments?.getString(PARAM_COMPANY_ID)!!
        if (DataHolder.hasData()) {
//            viewModel.companyLiveData.postValue(DataHolder.data)
            viewModel.companyResponse = DataHolder.data
        } else {
            viewModel.fetchCompany()
        }
        initView()
        subscribeToLiveData()
        setupToolbar()
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(CompanyDetailViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun showDialogMoreActions() {
        if (viewModel.companyLiveData.value != null) {
            val dialog =
                CompanyActionsDialogFragment
                    .newInstance(
                        viewModel.companyLiveData.value?.attributes?.currentUserAdmin ?: false
                    )
            dialog.show(childFragmentManager, null)
        }
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.company)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        btn_toolbar_more.setOnClickListener {
            showDialogMoreActions()
        }
    }

    private fun initView() {
        adapter.companyId = viewModel.companyId
        viewpager.adapter = adapter
        tab_layout.setupWithViewPager(viewpager)
    }

    private fun showData(company: CompanyResponse?) {
        company?.also {

            root_layout.visibility = View.VISIBLE

            GlideApp.with(this)
                .load(it.attributes?.photos?.medium)
                .apply(RequestOptions().circleCrop())
                .into(img)

            txt_name.text = it.attributes?.name

            val rating = it.attributes?.divercityRating?.totalDivercityRating
            if (rating != null) {
                lay_rating.visibility = View.VISIBLE
                rating_bar_header.rating = rating.toFloat()
                txt_rating.text = rating.toString()
            } else {
                lay_rating.visibility = View.GONE
            }

            if (it.attributes?.industry == null && it.attributes?.headquarters == null) {
                txt_subtitle1.visibility = View.GONE
            } else {
                txt_subtitle1.visibility = View.VISIBLE

                var subtitle = ""
                if (it.attributes?.industry != null)
                    subtitle = subtitle.plus(it.attributes?.industry)

                if (it.attributes?.industry != null && it.attributes?.headquarters != null)
                    subtitle.plus(" · ")

                if (it.attributes?.headquarters != null)
                    subtitle.plus(it.attributes?.headquarters)

                txt_subtitle1.text = subtitle
            }

            if (it.attributes?.companySize == null)
                txt_size.visibility = View.GONE
            else
                txt_size.text = it.attributes?.companySize
        }
    }

    private fun subscribeToLiveData() {
        viewModel.companyLiveData.observe(viewLifecycleOwner, Observer { group ->
            showData(group)
        })

        viewModel.fetchCompanyResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
//                    showProgress()
                }

                Status.ERROR -> {
//                    hideProgress()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
//                    hideProgress()
                }
            }
        })
    }

    private fun showToast(resId: Int) {
        Toast.makeText(context!!, resId, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String?) {
        Toast.makeText(context!!, message, Toast.LENGTH_SHORT).show()
    }

    override fun onShareViaMessage() {

    }

    override fun onShareToGroups() {

    }

    override fun onReportCompany() {

    }

    override fun onEditAdmins() {
        navigator.navigateToDeleteCompanyAdmin(
            this@CompanyDetailFragment,
            viewModel.companyId,
            ""
        )
    }

    override fun onAddAdmins() {
        navigator.navigateToCompanyAdmins(this@CompanyDetailFragment, viewModel.companyId)
    }
}