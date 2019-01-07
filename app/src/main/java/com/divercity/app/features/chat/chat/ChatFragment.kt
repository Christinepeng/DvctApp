package com.divercity.app.features.chat.chat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import com.divercity.app.BuildConfig
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.core.extension.networkInfo
import com.divercity.app.data.Status
import com.divercity.app.data.entity.chat.messages.ChatMessageResponse
import com.divercity.app.features.chat.chat.adapter.ChatAdapter
import com.divercity.app.repository.user.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import okhttp3.*
import okio.ByteString
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 24/12/2018.
 */

class ChatFragment : BaseFragment() {

    lateinit var viewModel: ChatViewModel

    var userName: String? = null
    var userId: String? = null

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var adapter: ChatAdapter

    private lateinit var client: OkHttpClient
    private var webSocket: WebSocket? = null

    var count = 700

    private var isLoadings = false
    private var hasLoadedAllItems = false

    companion object {

        private const val PARAM_USER_ID = "paramUserId"
        private const val PARAM_USER_NAME = "paramUserName"

        fun newInstance(userName: String, userId: String): ChatFragment {
            val fragment = ChatFragment()
            val arguments = Bundle()
            arguments.putString(PARAM_USER_ID, userId)
            arguments.putString(PARAM_USER_NAME, userName)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, viewModelFactory).get(ChatViewModel::class.java)
        } ?: throw Exception("Invalid Fragment")

        setHasOptionsMenu(true)

        userId = arguments?.getString(PARAM_USER_ID)
        userName = arguments?.getString(PARAM_USER_NAME)

        viewModel.getChatsIfExist(userId!!)
        viewModel.fetchOrCreateChat(userId!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(include_toolbar.toolbar)
            supportActionBar?.let {
                it.title = arguments?.getString(PARAM_USER_NAME)
                it.setDisplayHomeAsUpEnabled(true)
            }
        }

        subscribeToLiveData()
        setupView()
    }

    private fun setupView() {
        btn_send.setOnClickListener {
            if(et_msg.text.toString() != "")
                viewModel.sendMessage(et_msg.text.toString())
        }

        list.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    list.scrollToPosition(0)
                }
            }
        })

//        Paginate.with(list, object : Paginate.Callbacks{
//            override fun onLoadMore() {
//                Toast.makeText(activity, "onLoadMore", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun isLoading(): Boolean = false
//
//
//            override fun hasLoadedAllItems(): Boolean = hasLoadedAllItems
//
//        }).build()

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                checkEndOffset() // Each time when list is scrolled check if end of the list is reached
            }
        })
    }

    internal fun checkEndOffset() {
        val visibleItemCount = list.childCount
        val totalItemCount = list.layoutManager!!.itemCount

        val firstVisibleItemPosition: Int
        if (list.layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition = (list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else if (list.layoutManager is StaggeredGridLayoutManager) {
            // https://code.google.com/p/android/issues/detail?id=181461
            firstVisibleItemPosition = if (list.layoutManager!!.childCount > 0) {
                (list.layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(null)[0]
            } else {
                0
            }
        } else {
            throw IllegalStateException("LayoutManager needs to subclass LinearLayoutManager or StaggeredGridLayoutManager")
        }

        // Check if end of the list is reached (counting threshold) or if there is no items at all
        if (totalItemCount - visibleItemCount <= firstVisibleItemPosition || totalItemCount == 0) {

        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchCreateChatResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
//                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }

                Status.SUCCESS -> { }
            }
        })

        viewModel.fetchMessagesResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                }

                Status.ERROR -> {
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                }
            }
        })

        viewModel.sendMessageResponse.observe(this, Observer { response ->
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
                    et_msg.setText("")
                }
            }
        })

        viewModel.subscribeToPaginatedLiveData.observe(viewLifecycleOwner, Observer {
           subscribeToPagedListLiveData()
        })
    }

    private fun subscribeToPagedListLiveData(){
        viewModel.pagedListLiveData!!.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            hasLoadedAllItems = false
            isLoadings = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            val subscribeStr = "{\"command\": \"subscribe\",\"identifier\": \"{\\\"chat_id\\\": \\\"" +
                    viewModel.createChatResponse?.id +
                    "\\\",\\\"channel\\\":\\\"MessagesChannel\\\"}\"}"
            webSocket.send(subscribeStr)
            Timber.d("WebSocket onOpen: ".plus(subscribeStr))
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            Timber.d("WebSocket onMessage: ".plus(text))
            output("Receiving : " + text!!)

            try {
                // Parse message text
                val jsonParser = JsonParser()
                val response = jsonParser.parse(text)
                val message = response.asJsonObject.getAsJsonObject("message")

                val gson = Gson()
                if (message != null) {
                    val chat = gson.fromJson(message, ChatMessageResponse::class.java)
                    viewModel.insertChatDb(chat)
                }

            } catch (e: Exception) {
                // Message text not in JSON format or don't have {event}|{data} object
                Log.e("WebSocket", "Unknown message format.")
            }

        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
            output("Receiving bytes : " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
            webSocket.close(1000, null)
            output("Closing : $code / $reason")
            Timber.d("WebSocket onClosing: ".plus(code).plus(" - reason: ").plus(reason))
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Timber.d("WebSocket onFailure: ".plus(t.message))
            output("Error : " + t.message)
        }
    }

    private fun start() {
        val url = "wss://".plus(BuildConfig.BASE_URL).plus("/cable")
                .plus("?")
                .plus("token=").plus(userRepository.getAccessToken()).plus("&")
                .plus("client=").plus(userRepository.getClient()).plus("&")
                .plus("uid=").plus(userRepository.getUid())

        Log.i("WebSocket", "URL: ".plus(url))

        val request = Request.Builder()
                .url(url)
                .header("origin", "https://www.pincapp.com")
                .build()

        val listener = EchoWebSocketListener()
        client = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build()
        webSocket = client.newWebSocket(request, listener)
        client.dispatcher().executorService().shutdown()
    }

    private fun output(txt: String) {
//        runOnUiThread(Runnable { output.setText(output.getText().toString() + "\n\n" + txt) })
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        activity!!.registerReceiver(networkChangeReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        activity!!.unregisterReceiver(networkChangeReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocket?.close(1000, "bye")
        viewModel.closeSocket()
    }

    private val networkChangeReceiver = object : BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            val netInfo = context!!.networkInfo
            if (netInfo != null && netInfo.isConnected) {
                if(viewModel.hasFetchChatError){
                    viewModel.fetchOrCreateChat(userId!!)
                } else {
                    viewModel.checkIfReconnectionIsNeeded()
                }
            }
        }
    }
}