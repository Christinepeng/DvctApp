package com.divercity.android.core.bus

/**
 * Created by lucas on 14/01/2019.
 */

class RxEvent {
    data class EventUnauthorizedUser(val message: String)
    data class OnNewMessageReceived(val boolean: Boolean)
}