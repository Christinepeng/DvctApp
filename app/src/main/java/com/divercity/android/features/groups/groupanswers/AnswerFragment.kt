package com.divercity.android.features.groups.groupanswers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.base.BaseFragment
import com.divercity.android.core.extension.networkInfo
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.core.utils.Util
import com.divercity.android.data.Status
import com.divercity.android.features.groups.groupanswers.answeradapter.AnswerAdapter
import com.divercity.android.features.groups.groupanswers.answeradapter.AnswerViewHolder
import com.divercity.android.features.groups.groupanswers.model.Question
import com.divercity.android.model.UserMentionable
import com.divercity.android.model.user.User
import com.linkedin.android.spyglass.suggestions.SuggestionsResult
import com.linkedin.android.spyglass.suggestions.interfaces.Suggestible
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsResultListener
import com.linkedin.android.spyglass.suggestions.interfaces.SuggestionsVisibilityManager
import com.linkedin.android.spyglass.tokenization.QueryToken
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizer
import com.linkedin.android.spyglass.tokenization.impl.WordTokenizerConfig
import com.linkedin.android.spyglass.tokenization.interfaces.QueryTokenReceiver
import kotlinx.android.synthetic.main.fragment_answers.*
import kotlinx.android.synthetic.main.item_user_mention.view.*
import kotlinx.android.synthetic.main.view_image_btn_full.view.*
import kotlinx.android.synthetic.main.view_image_btn_small.view.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.util.*
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class AnswerFragment : BaseFragment(), QueryTokenReceiver, SuggestionsResultListener,
    SuggestionsVisibilityManager {

    lateinit var viewModel: AnswerViewModel

    @Inject
    lateinit var adapter: AnswerAdapter

    lateinit var userAdapter: UserMentionAdapter

    var isReplacing = false

    var question: Question? = null
    private var photoFile: File? = null

    companion object {

        private const val PARAM_QUESTION = "paramQuestionId"
        private const val BUCKET = "people-network"
        private val tokenizerConfig = WordTokenizerConfig
            .Builder()
            .setMaxNumKeywords(2)
            .setWordBreakChars(", ")
            .setExplicitChars("@")
            .setThreshold(1)
            .build()

        fun newInstance(question: Question): AnswerFragment {
            val fragment = AnswerFragment()
            val arguments = Bundle()
            arguments.putParcelable(PARAM_QUESTION, question)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_answers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AnswerViewModel::class.java)
        question = arguments?.getParcelable(PARAM_QUESTION)
        viewModel.start(question)

        KeyboardVisibilityEvent.setEventListener(activity!!) {
            if (it) {
                group_header.visibility = View.GONE
                item_quest_cardview_pic_main.visibility = View.GONE
            } else {
                group_header.visibility = View.VISIBLE
                if (question?.questionPicUrl != null) {
                    item_quest_cardview_pic_main.visibility = View.VISIBLE
                }
            }
        }

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.title = question?.groupTitle
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        subscribeToLiveData()
        setupView()
    }

    private fun setupView() {
        showProgressNoBk()

        et_msg.tokenizer = WordTokenizer(tokenizerConfig)
        et_msg.setQueryTokenReceiver(this)
        et_msg.setSuggestionsVisibilityManager(this)

        Glide
            .with(this)
            .load(question?.authorProfilePicUrl)
            .apply(RequestOptions().circleCrop())
            .into(img_user)

        txt_name.text = question?.authorName

        txt_date.text = Util.getTimeAgoWithStringServerDate(question?.createdAt)

        if (question?.questionPicUrl != null) {
            Glide
                .with(this)
                .load(question?.questionPicUrl)
                .into(item_quest_img_main)

            item_quest_img_main.setOnClickListener {
                lay_image_full_screen.visibility = View.VISIBLE
                GlideApp.with(this)
                    .load(question?.questionPicUrl)
                    .into(lay_image_full_screen.img_full_screen)
            }
        } else {
            item_quest_cardview_pic_main.visibility = View.GONE
        }

        lay_image.btn_remove_img.setOnClickListener {
            photoFile = null
            lay_image.visibility = View.GONE
        }

        btn_add_image.setOnClickListener {
            EasyImage.openChooserWithGallery(this, getString(R.string.pick_source), 0)
        }

        btn_send.setOnClickListener {
            //            if (et_msg.text.toString() != "" || photoFile != null) {
//                viewModel.sendNewAnswer(
//                    et_msg.text.toString(),
//                    ImageUtils.getStringBase64(photoFile, 600, 600)
//                )
//            }

            getData()
        }

        lay_image_full_screen.btn_close_full_screen_image.setOnClickListener {
            lay_image_full_screen.visibility = View.GONE
        }

        adapter.answerListener = object : AnswerViewHolder.Listener {

            override fun onImageTap(imageUrl: String) {
                lay_image_full_screen.visibility = View.VISIBLE
                GlideApp.with(this@AnswerFragment)
                    .load(imageUrl)
                    .into(lay_image_full_screen.img_full_screen)
            }
        }
        list.adapter = adapter
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (checkIfHasToScrollDown() && positionStart == 0) {
                    list.scrollToPosition(0)
                }
            }
        })

        txt_question.text = question?.question

//        et_msg.addTextChangedListener(object : TextWatcher {
//
//            override fun afterTextChanged(p0: Editable?) {
//                val message = p0.toString()
//                if (message != "" && !isReplacing) {
//                    val fullText = et_msg.text.toString().substring(0, et_msg.selectionStart)
//                    val lastWord = fullText.substring(fullText.lastIndexOf(" ") + 1)
//
//                    if (lastWord != "" && lastWord[0] == '@') {
//                        showList(true)
//                        viewModel.filterUserList(lastWord.substring(1))
//                    } else {
//                        showList(false)
//                    }
//                } else {
//                    showList(false)
//                    viewModel.filterUserList("")
//                }
//            }
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if (!isReplacing) {
//                    if (p2 > p3) {
//                        val spanStyle = et_msg.text.getSpans(p1, p1 + p2, StyleSpan::class.java)
////                        val spanUser = et_msg.text.getSpans(p1, p1 + p2, ChatMember::class.java)
//                        if (spanStyle.isNotEmpty()) {
//                            for (i in spanStyle.indices) {
//                                et_msg.text.removeSpan(spanStyle[i])
////                                et_msg.text.removeSpan(spanUser[i])
//                            }
//                        }
//                    }
//                }
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//        })

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy < 0)
                    checkEndOffset() // Each time when list is scrolled check if end of the list is reached
            }
        })


//        list_users.adapter = userAdapter
    }

    private fun getData() {
        val text = et_msg.mentionsText
        val mentionsSpan = text.mentionSpans
        var stringTxt = et_msg.text.toString()

        var amount = 0

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


//        mentionsSpan.map { (it.mention as UserMentionable).fullName = "<@U-" + (it.mention as UserMentionable).userId + ">"  }


        Toast.makeText(requireContext(), stringTxt, Toast.LENGTH_SHORT).show()


//        val editable = MentionsEditable(et_msg.text.toString())
//        mentionsSpan.forEachIndexed { pos, data ->
//            editable.setSpan(data, text.getSpanStart(data), text.getSpanEnd(data), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//            (editable.mentionSpans[pos].mention as UserMentionable).fullName = "<@U-" + (data.mention as UserMentionable).userId + ">"
//        }
//        val res = editable.toString()
//        Toast.makeText(requireContext(), res, Toast.LENGTH_SHORT).show()
    }

    private fun replaceMention(user: User) {
        isReplacing = true

        val fullTextTillCursor = et_msg.text.toString().substring(0, et_msg.selectionStart)
        val lastIndexOfAT = fullTextTillCursor.lastIndexOf("@")
        val textToInsert = "@".plus(user.name!!)

        et_msg.text.replace(
            lastIndexOfAT,
            et_msg.selectionStart,
            "@".plus(user.name)
        )
        viewModel.mentions.add(user)
        val bss = StyleSpan(Typeface.BOLD)
//        et_msg.text.setSpan(user.toChatMember(),
//            lastIndexOfAT,
//            lastIndexOfAT + textToInsert.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )

        et_msg.text.setSpan(
            bss,
            lastIndexOfAT,
            lastIndexOfAT + textToInsert.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        et_msg.setSelection(lastIndexOfAT + textToInsert.length)
        isReplacing = false
    }

    private fun checkIfHasToScrollDown(): Boolean {
        val firstVisibleItemPosition: Int
        if (list.layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition =
                (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else {
            throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager")
        }

        return firstVisibleItemPosition < 10
    }

    internal fun checkEndOffset() {
        val visibleItemCount = list.childCount
        val totalItemCount = list.layoutManager!!.itemCount

        val firstVisibleItemPosition: Int
        if (list.layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition =
                (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else {
            throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager")
        }

//        Timber.e("totalItemCount: " + totalItemCount + " - visibleItemCount: " + visibleItemCount + " - firstVisibleItemPosition: " + firstVisibleItemPosition)
        viewModel.checkIfFetchMoreData(visibleItemCount, totalItemCount, firstVisibleItemPosition)
    }

    private fun subscribeToLiveData() {
        viewModel.sendNewAnswerResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    btn_send.visibility = View.GONE
                    pb_sending_msg.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    btn_send.visibility = View.VISIBLE
                    pb_sending_msg.visibility = View.GONE

                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    btn_send.visibility = View.VISIBLE
                    pb_sending_msg.visibility = View.GONE
                    photoFile = null
                    lay_image.visibility = View.GONE
                    et_msg.setText("")
                }
            }
        })

//        viewModel.fetchChatMembersResponse.observe(viewLifecycleOwner, Observer { response ->
//            when (response?.status) {
//                Status.LOADING -> {
//
//                }
//                Status.ERROR -> {
//                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
//                }
//                Status.SUCCESS -> {
//                    userAdapter.data = response.data!!
//                    userAdapter.notifyDataSetChanged()
//                }
//            }
//        })

        viewModel.fetchAnswersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    if (response.data!!.page == 0)
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    if (response.data!!.answers.isEmpty() && response.data.page == 0) {
                        hideProgressNoBk()
                        txt_first_comment.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
            subscribeToPagedListLiveData()
        })

        viewModel.fetchGroupMembersResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    val result =
                        SuggestionsResult(response.data?.queryToken, response.data?.users)
                    onReceiveSuggestionsResult(result, BUCKET)
                }
            }
        })
    }

    private fun subscribeToPagedListLiveData() {
        viewModel.pagedListLiveData!!.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                txt_first_comment.visibility = View.GONE
                hideProgressNoBk()
            }
            adapter.submitList(it)
        })
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        activity!!.registerReceiver(networkChangeReceiver, intentFilter)
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(networkChangeReceiver)
        viewModel.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            activity,
            object : DefaultCallback() {
                override fun onImagePickerError(
                    e: Exception?,
                    source: EasyImage.ImageSource?,
                    type: Int
                ) {
                    //Some error handling
                    e!!.printStackTrace()
                    showToast(e.message)
                }

                override fun onImagesPicked(
                    imageFiles: List<File>,
                    source: EasyImage.ImageSource,
                    type: Int
                ) {
                    onPhotosReturned(imageFiles[0])
                }

                override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                    //Cancel handling, you might wanna remove taken photoFile if it was canceled
                    if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                        val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity!!)
                        photoFile?.delete()
                    }
                }
            })
    }

    private fun showToast(msg: String?) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun onPhotosReturned(file: File?) {
        lay_image.visibility = View.VISIBLE
        photoFile = file
        GlideApp.with(this)
            .load(file)
            .into(lay_image.img_added)
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            val netInfo = context!!.networkInfo
            if (netInfo != null && netInfo.isConnected) {
                viewModel.checkErrorsToReconnect()
            }
        }
    }

    override fun onQueryReceived(queryToken: QueryToken): MutableList<String> {
        val buckets = Arrays.asList<String>(BUCKET)
        if (queryToken.tokenString.startsWith("@")) {
            viewModel.fetchGroupMembers(
                question!!.groupId.toString(),
                0,
                10,
                queryToken.tokenString.substring(1),
                queryToken
            )
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
                    et_msg.insertMention(data)
                    list_users.swapAdapter(UserMentionAdapter(ArrayList()), true)
                    displaySuggestions(false)
                    et_msg.requestFocus()
                }
            }
        }
    }
}