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

/**
 * Created by lucas on 18/03/2018.
 */

class RateCompanyDiversityDialogFragment : BaseDialogFragment() {

    lateinit var viewModel: RateCompanyDiversityDialogViewModel
    var listener: Listener? = null

    private lateinit var dialogView: View

    companion object {

        fun newInstance(company: CompanyResponse): RateCompanyDiversityDialogFragment {
            DataHolder.data = company
            return RateCompanyDiversityDialogFragment()
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
        subscribeToLiveData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        dialogView = inflater.inflate(R.layout.dialog_rate_company, null)

        if (DataHolder.hasData()) {
            val company =
                DataHolder.data
            dialogView.apply {

                GlideApp.with(this)
                    .load(company?.attributes?.photos?.medium)
                    .into(img)

                txt_name.text = company?.attributes?.name

                rating_bar.onRatingBarChangeListener =
                    RatingBar.OnRatingBarChangeListener { _, p1, _ ->
                        if (p1 < 1.0f)
                            rating_bar.rating = 1.0f
                        txt_title.visibility = View.GONE
                        btn_submit.visibility = View.VISIBLE
                        lay_review.visibility = View.VISIBLE
                    }

                txt_title.text = getString(R.string.rate_company_label, company?.attributes?.name)

                btn_close.setOnClickListener {
                    listener?.onCompanyRated()
                    dismiss()
                }

                btn_submit.setOnClickListener {
                    viewModel.rateCompany(
                        company?.id!!,
                        rating_bar.rating.toInt(),
                        et_review.text.toString()
                    )
                }
            }
        }
        builder.setView(dialogView)
        return builder.create()
    }

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