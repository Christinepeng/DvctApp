package com.divercity.android.features.company.companyaddadmin.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divercity.android.model.user.User
import javax.inject.Inject

class AdminSelectedAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var admins: ArrayList<User>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun getAdminsIds() : List<String>{
        val usersId = ArrayList<String>()
        admins?.forEach {
            usersId.add(it.id)
        }
        return usersId
    }

    var listener: AdminSelectedViewHolder.Listener? = null

    override fun getItemCount(): Int {
        return admins?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AdminSelectedViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AdminSelectedViewHolder).bindTo(admins?.get(position))
    }
}