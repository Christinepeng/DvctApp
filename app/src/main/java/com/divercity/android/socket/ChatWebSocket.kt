package com.divercity.android.socket

import com.divercity.android.core.utils.MySocket
import com.divercity.android.data.entity.chat.messages.ChatMessageEntityResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 11/01/2019.
 */

class ChatWebSocket @Inject
constructor(private val socket: MySocket) {

    companion object {
        const val EVENT_CHAT_NEW_MESSAGE = "new_message"
        const val EVENT_TYPING = "rtm_typing"
    }

    var listener: Listener? = null

    fun connect(chatId: String) {

//        Subscribing to chat channel
        socket.onEvent(MySocket.EVENT_OPEN, object : MySocket.OnEventListener() {

            override fun onMessage(event: String?) {
                val identifier =
                    "\"{\\\"chat_id\\\": \\\"" + chatId + "\\\",\\\"channel\\\":\\\"MessagesChannel\\\"}\"}"
                Timber.d("onOpen: ".plus(identifier))
                socket.send("subscribe", identifier)
            }
        })

        socket.onEvent(MySocket.EVENT_RECONNECT_ATTEMPT, object : MySocket.OnEventListener() {

            override fun onMessage(event: String?) {

            }
        })

        socket.setMessageListener(object : MySocket.OnMessageListener() {

            override fun onMessage(data: String?) {

                try {
                    val parser = JsonParser()
                    val response = parser.parse(data).asJsonObject

                    if (response.has("identifier") && response.has("message")) {
                        val message = response.getAsJsonObject("message")

                        if (message.has("event_type")) {
                            when (message.get("event_type").asString) {
                                EVENT_CHAT_NEW_MESSAGE -> {
                                    val chat =
                                        Gson().fromJson(message, ChatMessageEntityResponse::class.java)
                                    listener?.onChatMessageReceived(chat)
                                }
                                EVENT_TYPING -> {

                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Timber.v("Unknown message format.")
                }
            }

        })

        socket.connect()
    }

    fun getSocketState(): MySocket.State {
        return socket.state
    }

    fun close() {
        socket.close()
    }

    fun stopTryingToReconnect() {
        socket.stopTryingToReconnect()
    }

    interface Listener {

        fun onChatMessageReceived(chat: ChatMessageEntityResponse)

        fun onSocketOpen()
    }
}