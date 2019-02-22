package com.divercity.android.socket

import com.divercity.android.core.utils.MySocket
import com.divercity.android.core.utils.Util
import com.divercity.android.data.entity.group.answer.response.AnswerResponse
import com.divercity.android.data.entity.group.answer.response.Attributes
import com.divercity.android.data.entity.group.answer.response.AuthorInfo
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lucas on 11/01/2019.
 */

class AnswersWebSocket @Inject
constructor(private val socket: MySocket) {

    var listener: Listener? = null

    fun connect(questionId: String) {

//        Subscribing to chat channel
        socket.onEvent(MySocket.EVENT_OPEN, object : MySocket.OnEventListener() {

            override fun onMessage(event: String?) {
                val identifier =
                    "\"{\\\"question_id\\\": \\\"" + questionId + "\\\",\\\"channel\\\":\\\"AnswersChannel\\\"}\"}"
                Timber.d("onOpen: ".plus(identifier))
                socket.send("subscribe", identifier)
            }
        })

        socket.setMessageListener(object : MySocket.OnMessageListener() {

            override fun onMessage(data: String?) {

                try {
                    val parser = JsonParser()
                    val response = parser.parse(data).asJsonObject

                    if (response.has("identifier") && response.has("message")) {
                        val message = response.getAsJsonObject("message")

                        if (message.has("question_type")) {
                            val r = Gson().fromJson(message, SocketAnswerResponse::class.java)
                            val answerResponse = AnswerResponse(
                                Attributes(
                                    text = r.answerText,
                                    rawText = r.answerText,
                                    authorId = r.answerAuthorId,
                                    createdAt = Util.getDateWithServerTimeStamp(r.createdAt),
                                    questionId = r.questionId,
                                    authorInfo = AuthorInfo(
                                        name = r.answerAuthorName,
                                        avatarMedium = r.answerAuthorAvatarMedium,
                                        avatarThumb = r.answerAuthorAvatarThumb,
                                        nickname = r.answerAuthorNickname
                                    ),
                                    images = r.answerImages
                                ),
                                id = r.answerId.toString()
                            )
                            listener?.onAnswerReceived(answerResponse)
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
        socket.terminate()
    }

    fun stopTryingToReconnect() {
        socket.stopTryingToReconnect()
    }

    interface Listener {
        fun onAnswerReceived(answer: AnswerResponse)
    }

    data class SocketAnswerResponse(

        @field:SerializedName("group_of_interest_id")
        val groupOfInterestId: Int? = null,

        @field:SerializedName("answer_text")
        val answerText: String? = null,

        @field:SerializedName("answer_author_name")
        val answerAuthorName: String? = null,

        @field:SerializedName("answer_images")
        val answerImages: List<String>? = null,

        @field:SerializedName("question_type")
        val questionType: String? = null,

        @field:SerializedName("answer_author_nickname")
        val answerAuthorNickname: String? = null,

        @field:SerializedName("answer_author_id")
        val answerAuthorId: Int? = null,

        @field:SerializedName("answer_author_avatar_medium")
        val answerAuthorAvatarMedium: String? = null,

        @field:SerializedName("created_at")
        val createdAt: String? = null,

        @field:SerializedName("question_id")
        val questionId: Int? = null,

        @field:SerializedName("answer_id")
        val answerId: Int? = null,

        @field:SerializedName("answer_author_avatar_thumb")
        val answerAuthorAvatarThumb: String? = null
    )
}