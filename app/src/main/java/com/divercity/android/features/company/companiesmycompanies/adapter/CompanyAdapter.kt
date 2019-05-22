package com.divercity.android.features.company.companiesmycompanies.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.data.entity.company.response.CompanyResponse
import com.divercity.android.features.company.companies.adapter.TabCompaniesViewHolder
import javax.inject.Inject

/**
 * Created by lucas on 17/10/2018.
 */

class CompanyAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CompanyResponse> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var listener: TabCompaniesViewHolder.Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TabCompaniesViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TabCompaniesViewHolder).bindTo(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}