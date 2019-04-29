package com.divercity.android.features.groups.groupanswers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.model.usermentionable.UserMentionable
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import com.linkedin.android.spyglass.ui.MentionsEditText
import kotlinx.android.synthetic.main.item_user_mention.view.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by lucas on 2019-04-29.
 */

class UserMentionWrapper @Inject
constructor() : QueryTokenReceiver, SuggestionsResultListener,
    SuggestionsVisibilityManager {

    lateinit var list_users: RecyclerView
    lateinit var fetchUsers: (String, QueryToken) -> Unit
    lateinit var mentionsEdTxt: MentionsEditText
    private lateinit var userAdapter : UserMentionAdapter

    companion object {
        const val BUCKET = "people-network"
    }

    fun setEditTextTokenize( edTxt : MentionsEditText, tokenizer: WordTokenizerConfig){
        edTxt.tokenizer = WordTokenizer(tokenizer)
        edTxt.setQueryTokenReceiver(this)
        edTxt.setSuggestionsVisibilityManager(this)
    }

    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        val buckets = Arrays.asList<String>(BUCKET)
        if (queryToken.tokenString.startsWith("@") && queryToken.tokenString.length > 1) {
            fetchUsers(queryToken.tokenString.substring(1), queryToken)
        } else {
            displaySuggestions(false)
        }
        return buckets
    }

    override fun onReceiveSuggestionsResult(result: SuggestionsResult, bucket: String) {
        val suggestions = result.suggestions
        userAdapter = UserMentionAdapter(result.suggestions)
        list_users.swapAdapter(userAdapter, true)
        val display = suggestions != null && suggestions.size > 0
        displaySuggestions(display)
    }

    override fun displaySuggestions(display: Boolean) {
        if (display) {
            list_users.visibility = View.VISIBLE
        } else {
            list_users.visibility = View.GONE
        }
    }

    override fun isDisplayingSuggestions(): Boolean {
        return list_users.visibility == View.VISIBLE
    }


    inner class UserMentionAdapter
    constructor(var users: List<Suggestible>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int {
            return users.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user_mention, parent, false)
            return UserMentionViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as UserMentionViewHolder).bindTo(users[position] as UserMentionable)
        }
    }

    inner class UserMentionViewHolder
    constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bindTo(data: UserMentionable?) {
            data?.let {
                GlideApp.with(itemView)
                    .load(data.pictureUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(itemView.img)

                itemView.txt_name.text = data.fullName

                itemView.setOnClickListener {
                    mentionsEdTxt.insertMention(data)
                    list_users.swapAdapter(UserMentionAdapter(ArrayList()), true)
                    displaySuggestions(false)
                    mentionsEdTxt.requestFocus()
                }
            }
        }
    }

    fun getMessageWithMentions() : String{
        val text = mentionsEdTxt.mentionsText
        val mentionsSpan = text.mentionSpans
        var stringTxt = mentionsEdTxt.text.toString()

        var amount = 0

//      It is sorted because you can get wrong behaviour when your insert a tag manually before
//      another tag.
        mentionsSpan.sortBy {
            text.getSpanStart(it)
        }

        mentionsSpan.forEach {
            val start = text.getSpanStart(it)
            val end = text.getSpanEnd(it)

            val name = text.subSequence(start, end).toString()
            val replacement = "<@U-" + (it.mention as UserMentionable).userId + ">"

            stringTxt = stringTxt.replaceRange(
                start - amount,
                end - amount,
                replacement
            )

            amount += name.length - replacement.length
        }

        return stringTxt
    }

}