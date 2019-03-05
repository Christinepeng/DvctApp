package com.divercity.android.features.activity.notifications.adapter

import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.RequestOptions
import com.divercity.android.R
import com.divercity.android.core.utils.GlideApp
import com.divercity.android.data.entity.activity.notification.NotificationResponse
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationsViewHolder
private constructor(
    itemView: View,
    private val listener: Listener?
) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(position: Int, data: NotificationResponse?) {
        data?.let {
            itemView.apply {

                GlideApp.with(this)
                    .load(it.attributes?.lastActiveUserInfo?.avatarThumb)
                    .apply(RequestOptions().circleCrop())
                    .into(img_user)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    txt_notification.text =
                        Html.fromHtml(data.attributes?.message, Html.FROM_HTML_MODE_COMPACT)
                else
                    txt_notification.text = Html.fromHtml(data.attributes?.message)

                if (it.attributes?.read!!)
                    main_layout.setBackgroundColor(
                        ContextCompat.getColor(
                            this.context,
                            R.color.white
                        )
                    )
                else
                    main_layout.setBackgroundColor(
                        ContextCompat.getColor(
                            this.context,
                            R.color.splash_background
                        )
                    )


                txt_date.text = data.attributes?.updatedAgoInWords
            }
        }
    }

    interface Listener {

        fun onNotificationClick(notification: NotificationResponse)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener?
        ): NotificationsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_notification, parent, false)
            return NotificationsViewHolder(view, listener)
        }
    }
}
