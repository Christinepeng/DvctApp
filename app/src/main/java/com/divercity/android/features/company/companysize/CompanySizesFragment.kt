package com.divercity.android.features.company.companysize

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.sizes.CompanySizeResponse
import kotlinx.android.synthetic.main.fragment_job_type.*
import kotlinx.android.synthetic.main.view_retry.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import javax.inject.Inject

class CompanySizesFragment : BaseFragment() {

    lateinit var viewModel: CompanySizesViewModel

    @Inject
    lateinit var adapter: CompanySizeAdapter

    companion object {

        const val COMPANY_SIZE_PICKED = "companySizePicked"

        fun newInstance(): CompanySizesFragment {
            return CompanySizesFragment()
        }
    }

    override fun layoutId(): Int = R.layout.fragment_job_type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[CompanySizesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupView()
        subscribeToLiveData()
    }

    private fun setupToolbar() {
        (activity as CompanySizesActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.setTitle(R.string.select_company_size)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupView() {
        adapter.listener = listener
        list.adapter = adapter
    }

    private fun subscribeToLiveData() {
        viewModel.fetchCompanySizeResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showRetry(false)
                    showProgressNoBk()
                }

                Status.ERROR -> {
                    showRetry(true)
                    hideProgressNoBk()
                    showToast(response.message)
                }

                Status.SUCCESS -> {
                    showRetry(false)
                    hideProgressNoBk()
                    adapter.submitList(response.data)
                }
            }
        })
    }

    private fun showRetry(boolean: Boolean) {
        if (boolean) {
            include_retry.btn_retry.setOnClickListener {
                viewModel.fetchJobTypes()
            }
            include_retry.visibility = View.VISIBLE
        } else
            include_retry.visibility = View.GONE
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private val listener: CompanySizeAdapter.Listener = object : CompanySizeAdapter.Listener {

        override fun onCompanySizeClick(size: CompanySizeResponse) {
            val intent = Intent()
            intent.putExtra(COMPANY_SIZE_PICKED, size)
            activity?.apply {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}