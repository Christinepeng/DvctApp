package com.divercity.android.features.chat.newchat.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.divercity.android.R
import kotlinx.android.synthetic.main.item_contact_character.view.*

class CharacterViewHolder
private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(name : Char?) {
        itemView.apply {
            name?.let {
                item_character.text = it.toString()
            }
        }
    }
    companion object {

        fun create(parent: ViewGroup): CharacterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_contact_character, parent, false)
            return CharacterViewHolder(view)
        }
    }
}
