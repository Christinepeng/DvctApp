package com.divercity.android.features.dialogs.ratecompany

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.divercity.android.R
import com.divercity.android.core.base.BaseDialogFragment
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.Status
import com.divercity.android.data.entity.company.response.CompanyResponse
import kotlinx.android.synthetic.main.dialog_rate_company.view.*
import kotlinx.android.synthetic.main.view_rate_company.view.*

/**
 * Created by lucas on 18/03/2018.
 */

class RateCompanyDiversityDialogFragment : BaseDialogFragment() {

    lateinit var viewModel: RateCompanyDiversityDialogViewModel
    var listener: Listener? = null

    private lateinit var dialogView: View
    var onClose: (() -> Unit)? = null

    companion object {

        private const val COMPANY_ID = "companyId"

        fun newInstance(company: CompanyResponse): RateCompanyDiversityDialogFragment {
            DataHolder.data = company
            return RateCompanyDiversityDialogFragment()
        }

        fun newInstance(companyId: String): RateCompanyDiversityDialogFragment {
            val fragment = RateCompanyDiversityDialogFragment()
            val arguments = Bundle()
            arguments.putString(COMPANY_ID, companyId)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            viewModelFactory
        )[RateCompanyDiversityDialogViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_rate_company, null)
        subscribeToLiveData()

        if (DataHolder.hasData()) {
            val company =
                DataHolder.data
            showData(company)
        } else {
            viewModel.fetchCompany(arguments?.getString(COMPANY_ID)!!)
        }
        builder.setView(dialogView)
        return builder.create()
    }

    private fun showData(company: CompanyResponse?) {
        dialogView.apply {
            company?.also {

                GlideApp.with(this)
                    .load(it.attributes?.photos?.medium)
                    .into(img)

                txt_company_name.text = it.attributes?.name

                btn_submit.setOnClickListener {
                    viewModel.rateCompany(
                        company.id!!,
                        getRatingValue(rating_bar_gender),
                        getRatingValue(rating_bar_race),
                        getRatingValue(rating_bar_age),
                        getRatingValue(rating_bar_sex_orien),
                        getRatingValue(rating_bar_abel_bodiedness),
                        et_review.text.toString()
                    )
                }
            }

            btn_close.setOnClickListener {
                dismiss()
                onClose?.invoke()
            }

        }
    }

    private fun getRatingValue(ratingBar: RatingBar): Int = ratingBar.rating.toInt()

    fun subscribeToLiveData() {
        viewModel.rateCompanyResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    listener?.onCompanyRated()
                    dismiss()
                }
            }
        })

        viewModel.fetchCompanyResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> showProgress()

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    hideProgress()
                    showData(response.data)
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    interface Listener {

        fun onCompanyRated()
    }

    private fun showProgress() = progressStatus(View.VISIBLE)

    private fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        dialogView.include_loading.visibility = viewStatus
    }
}