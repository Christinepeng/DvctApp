package com.divercity.android.features.home.people.companies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.company.response.CompanyResponse
import kotlinx.android.synthetic.main.item_company.view.*

class TabCompaniesViewHolder private constructor(itemView: View, private val listener: Listener) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(data: CompanyResponse?) {
        itemView.apply {
            data?.let {
                GlideApp.with(this)
                    .load(it.attributes.photos.medium)
                    .into(item_img)

                item_txt_title.text = it.attributes.name
                item_txt_subtitle1.visibility = View.GONE
                item_txt_subtitle2.visibility = View.GONE

                setOnClickListener { listener.onCompanyClick(data) }
            }
        }
    }

    interface Listener {

        fun onCompanyClick(company: CompanyResponse)
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): TabCompaniesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_company, parent, false)
            return TabCompaniesViewHolder(view, listener)
        }
    }
}
