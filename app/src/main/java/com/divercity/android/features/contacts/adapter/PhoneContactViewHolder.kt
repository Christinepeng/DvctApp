package com.divercity.android.features.contacts.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import com.divercity.android.features.contacts.model.PhoneContact
import kotlinx.android.synthetic.main.item_phone_contact.view.*

class PhoneContactViewHolder
private constructor(itemView: View, private val listener : Listener?) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(isSelected: Boolean, data: PhoneContact?) {
        data?.let {
            itemView.apply {
                btn_select.isSelected  = isSelected
                btn_select.setOnClickListener {
                    btn_select.isSelected = !btn_select.isSelected
                    listener?.onUserSelect(data, btn_select.isSelected)
                }
                txt_name.text = it.name
                txt_phone.text = it.phoneNumber
            }
        }
    }

    interface Listener {
        fun onUserSelect(data : PhoneContact, isSelected : Boolean)
    }

    companion object {

        fun create(parent: ViewGroup, listener : Listener?): PhoneContactViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_phone_contact, parent, false)
            return PhoneContactViewHolder(view, listener)
        }
    }
}
