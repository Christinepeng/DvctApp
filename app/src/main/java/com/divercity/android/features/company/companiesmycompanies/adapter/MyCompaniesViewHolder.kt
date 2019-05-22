package com.divercity.android.features.company.companiesmycompanies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.R
import kotlinx.android.synthetic.main.view_my_companies.view.*

class MyCompaniesViewHolder private constructor(itemView: View, val listener: Listener?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindTo(listSize: Int) {
        itemView.apply {

            btn_see_all.setOnClickListener {
                listener?.onSeeAll()
            }

            if (listSize == 3) {
                btn_see_all.visibility = View.VISIBLE
            } else {
                btn_see_all.visibility = View.GONE
            }
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            adapter: CompanyAdapter,
            listener: Listener?
        ): MyCompaniesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_my_companies, parent, false)
            view.list_my_companies.adapter = adapter
            return MyCompaniesViewHolder(view, listener)
        }
    }

    interface Listener {

        fun onSeeAll()
    }
}
