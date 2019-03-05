package com.divercity.android.features.contacts.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.divercity.android.features.contacts.model.PhoneContact
import javax.inject.Inject

class PhoneContactAdapter @Inject
constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var fullDataList : List<PhoneContact>? = null
    private var lastSearchQuery: String? = ""

    var data: List<PhoneContact>? = null
    var selectedContacts = HashSet<PhoneContact>()

    var listener= object : PhoneContactViewHolder.Listener {

        override fun onUserSelect(data: PhoneContact, isSelected : Boolean) {
            if (isSelected) selectedContacts.add(data) else selectedContacts.remove(data)
        }
    }

    fun getSelectedPhoneNumbers() : List<String>{
        val phoneNumbers = ArrayList<String>()
        selectedContacts.forEach {
            phoneNumbers.add(it.phoneNumber?.replace("-","")!!)
        }
        return phoneNumbers
    }

    fun setListData(data: List<PhoneContact>) {
        this.data = data
        fullDataList = data.toMutableList()
        notifyDataSetChanged()
    }

    fun search(search : String?){
        if(search != null && search != lastSearchQuery) {
            data = fullDataList?.filter {
                it.name!!.toLowerCase().contains(search.toLowerCase())
            }
        } else if(search == ""){
            data = fullDataList
        }
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhoneContactViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhoneContactViewHolder).bindTo(
            selectedContacts.contains(data!![position]),
            data?.get(position))
    }
}