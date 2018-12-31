package com.divercity.app.features.chat.chat

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import com.divercity.app.BuildConfig
import com.divercity.app.R
import com.divercity.app.core.base.BaseFragment
import com.divercity.app.data.Status
import com.divercity.app.repository.user.UserRepository
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.view_toolbar.view.*
import okhttp3.*
import okio.ByteString
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

    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

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

        setupView()
        subscribeToLiveData()
        start()
    }

    private fun setupView() {
        btn_send.setOnClickListener {
//            val msge = "{\"message\":{\"chat_id\"".plus() "in JSON format\", }}"
//            webSocket?.send(et_msg.text.toString())
        }
    }

    private fun subscribeToLiveData() {
        viewModel.fetchCreateChatResponse.observe(this, Observer { response ->
            when (response?.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.ERROR -> {
                    hideProgress()
                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                }
                Status.SUCCESS -> {
                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private inner class EchoWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
//            webSocket.send("Hello, it's SSaurel !")
//            webSocket.send("What's up ?")
//            webSocket.send(ByteString.decodeHex("deadbeef"))
//            webSocket.close(1000, "Goodbye !")
        }

        override fun onMessage(webSocket: WebSocket?, text: String?) {
            output("Receiving : " + text!!)
        }

        override fun onMessage(webSocket: WebSocket?, bytes: ByteString) {
            output("Receiving bytes : " + bytes.hex())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
            webSocket.close(1000, null)
            output("Closing : $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
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
        webSocket = client.newWebSocket(request, listener)
        client.dispatcher().executorService().shutdown()
    }

    private fun output(txt: String) {
//        runOnUiThread(Runnable { output.setText(output.getText().toString() + "\n\n" + txt) })
    }

    override fun onDestroy() {
        super.onDestroy()
//        webSocket?.close(0, "bye")
    }
}