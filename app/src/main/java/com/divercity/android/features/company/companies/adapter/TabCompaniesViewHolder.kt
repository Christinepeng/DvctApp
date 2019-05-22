package com.divercity.android.features.company.companies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.company.response.CompanyResponse
import kotlinx.android.synthetic.main.item_company.view.*

class TabCompaniesViewHolder private constructor(itemView: View, private val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: CompanyResponse?) {
        itemView.apply {
            data?.attributes?.let {
                GlideApp.with(this)
                    .load(it.photos?.medium)
                    .into(item_img)

                item_txt_title.text = it.name

                if (it.industry != null) {
                    item_txt_subtitle1.visibility = View.VISIBLE
                    item_txt_subtitle1.text = it.industry
                } else {
                    item_txt_subtitle1.visibility = View.GONE
                }

                if(it.headquarters != null){
                    item_txt_subtitle2.visibility = View.VISIBLE
                    item_txt_subtitle2.text = it.headquarters
                } else {
                    item_txt_subtitle2.visibility = View.GONE
                }

                if(it.companySize != null){
                    item_txt_subtitle3.visibility = View.VISIBLE
                    item_txt_subtitle3.text = it.companySize
                } else {
                    item_txt_subtitle3.visibility = View.GONE
                }

                setOnClickListener { listener?.onCompanyClick(data) }
            }
        }
    }

    interface Listener {

        fun onCompanyClick(company: CompanyResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener?): TabCompaniesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_company, parent, false)
            return TabCompaniesViewHolder(view, listener)
        }
    }
}
