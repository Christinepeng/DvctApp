package com.divercity.android.core.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.JsonObject;

import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;
import timber.log.Timber;

/**
 * Websocket class based on OkHttp3 with {event->data} message format to make your life easier.
 */

public class MySocket {

    private final static String TAG = MySocket.class.getSimpleName();
    private final static String CLOSE_REASON = "End of session";
    private final static int MAX_COLLISION = 7;

    public final static String EVENT_OPEN = "open";
    public final static String EVENT_RECONNECT_ATTEMPT = "reconnecting";
    public final static String EVENT_CLOSED = "closed";

    /**
     * Websocket state
     */
    private static State state;
    /**
     * Websocket main request
     */
    private Request request;
    /**
     * Websocket connection
     */
    private RealWebSocket realWebSocket;
    /**
     * Reconnection post delayed handler
     */
    private static Handler delayedReconnection;
    /**
     * Websocket events listeners
     */
    private Map<String, OnEventListener> eventListener;
    /**
     * Websocket events new message listeners
     */
    private static Map<String, OnEventResponseListener> eventResponseListener;
    /**
     * Message list tobe send onEvent open {@link State#OPEN} connection state
     */
    private static Map<String, String> onOpenMessageQueue = new HashMap<>();
    /**
     * Websocket state change listener
     */
    private static OnStateChangeListener onChangeStateListener;
    /**
     * Websocket new message listener
     */
    private static OnMessageListener messageListener;
    /**
     * Number of reconnection attempts
     */
    private static int reconnectionAttempts;
    private static boolean skipOnFailure;

    private OkHttpClient.Builder httpClientBuilder;

    /**
     * Main socket states
     */
    public enum State {
        CLOSED, CLOSING, CONNECT_ERROR, RECONNECT_ATTEMPT, RECONNECTING, OPENING, OPEN
    }

    public static class Builder {

        private Request.Builder request;
        private OkHttpClient.Builder httpClientBuilder;

        private Builder(Request.Builder request, OkHttpClient.Builder httpClientBuilder) {
            this.request = request;
            this.httpClientBuilder = httpClientBuilder;
        }

        public static Builder with(@NonNull String url) {
            // Silently replace web socket URLs with HTTP URLs.
            if (!url.regionMatches(true, 0, "ws:", 0, 3) && !url.regionMatches(true, 0, "wss:", 0, 4))
                throw new IllegalArgumentException("web socket url must start with ws or wss, passed url is " + url);

            return new Builder(new Request.Builder().url(url), new OkHttpClient.Builder());
        }

        public Builder setPingInterval(long interval, @NonNull TimeUnit unit) {
//            httpClient.pingInterval(interval, unit);
            return this;
        }

        public Builder addHttpLogginInterceptor(HttpLoggingInterceptor interceptor) {
            httpClientBuilder.addInterceptor(interceptor);
            return this;
        }

        public Builder addHeader(@NonNull String name, @NonNull String value) {
            request.addHeader(name, value);
            return this;
        }

        public MySocket build() {
            return new MySocket(request.build(), httpClientBuilder);
        }
    }

    private MySocket(Request request, OkHttpClient.Builder httpClientBuilder) {
        this.request = request;
        this.httpClientBuilder = httpClientBuilder;
        state = State.CLOSED;
        eventListener = new HashMap<>();
        eventResponseListener = new HashMap<>();
        delayedReconnection = new Handler(Looper.getMainLooper());
        skipOnFailure = false;
    }

    /**
     * Start socket connection if i's not already started
     */
    public MySocket connect() {
        if (httpClientBuilder == null)
            throw new IllegalStateException("Make sure to use Socket.Builder before using Socket#connect.");
        if (realWebSocket == null) {
            OkHttpClient okHttpClient = httpClientBuilder.build();
            realWebSocket = (RealWebSocket) okHttpClient.newWebSocket(request, webSocketListener);
//            okHttpClient.dispatcher().executorService().shutdown();
            changeState(State.OPENING);
        } else if (state == State.CLOSED) {
            realWebSocket.connect(httpClientBuilder.build());
            changeState(State.OPENING);
        }
        return this;
    }

    /**
     * Set listener which fired every time message received with contained data.
     *
     * @param listener message on arrive listener
     */
    public MySocket onEvent(@NonNull String event, @NonNull OnEventListener listener) {
        eventListener.put(event, listener);
        return this;
    }

    /**
     * Set listener which fired every time message received with contained data.
     *
     * @param listener message on arrive listener
     */
    public MySocket onEventResponse(@NonNull String event, @NonNull OnEventResponseListener listener) {
        eventResponseListener.put(event, listener);
        return this;
    }

    /**
     * Send message in {event->data} format
     *
     * @param command event name that you want sent message to
     * @param identifier  message data in JSON format
     * @return true if the message send/on socket send quest; false otherwise
     */
    public boolean send(@NonNull String command, @NonNull String identifier) {
        String text = "{\"command\": \""+command+"\",\"identifier\": " + identifier;
        return realWebSocket.send(text);
//        try {
////            JSONObject text = new JSONObject();
////            text.put("command", event);
////            text.put("identifier", data);
////            Log.v(TAG, "Try to send data " + text.toString());
//
//            String text = "";
//            return realWebSocket.send(text.toString());
//        } catch (JSONException e) {
//            Log.e(TAG, "Try to send data with wrong JSON format, data: " + data);
//        }
    }

    /**
     * Set state listener which fired every time {@link MySocket#state} changed.
     *
     * @param listener state change listener
     */
    public MySocket setOnChangeStateListener(@NonNull OnStateChangeListener listener) {
        onChangeStateListener = listener;
        return this;
    }

    /**
     * Message listener will be called in any message received even if it's not
     * in a {event -> data} format.
     *
     * @param listener message listener
     */
    public MySocket setMessageListener(@NonNull OnMessageListener listener) {
        messageListener = listener;
        return this;
    }

    public void removeEventListener(@NonNull String event) {
        eventListener.remove(event);
        onOpenMessageQueue.remove(event);
    }

    /**
     * Clear all socket listeners in one line
     */
    public void clearListeners() {
        eventListener.clear();
        messageListener = null;
        onChangeStateListener = null;
    }

    /**
     * Send normal close request to the host
     */
    public void close() {
        if (realWebSocket != null) {
            realWebSocket.close(1000, CLOSE_REASON);
        }
    }

    /**
     * Send close request to the host
     */
    public void close(int code, @NonNull String reason) {
        if (realWebSocket != null) {
            realWebSocket.close(code, reason);
        }
    }

    /**
     * Terminate the socket connection permanently
     */
    public void terminate() {
        skipOnFailure = true; // skip onFailure callback
        if (realWebSocket != null) {
            realWebSocket.cancel(); // close connection
            realWebSocket = null; // clear socket object
        }
    }

    /**
     * Add message in a queue if the socket not open and send them
     * if the socket opened
     *
     * @param command    command name that you want sent message to
     * @param identifier message identifier in JSON format
     */
    public void sendOnOpen(@NonNull String command, @NonNull String identifier) {
        if (state != State.OPEN)
            onOpenMessageQueue.put(command, identifier);
        else
            send(command, identifier);
    }

    /**
     * Retrieve current socket connection state {@link State}
     */
    public State getState() {
        return state;
    }

    /**
     * Change current state and call listener method with new state
     * {@link OnStateChangeListener#onChange(MySocket, State)}
     *
     * @param newState new state
     */
    private void changeState(State newState) {
        state = newState;
        if (onChangeStateListener != null) {
            onChangeStateListener.onChange(MySocket.this, state);
        }
    }

    public void stopTryingToReconnect() {
        delayedReconnection.removeCallbacksAndMessages(null);
        if (realWebSocket != null) {
            // Cancel websocket connection
            realWebSocket.cancel();
            // Clear websocket object
            realWebSocket = null;
        }
    }

    /**
     * Try to reconnect to the websocket after delay time using <i>Exponential backoff</i> method.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Exponential_backoff"></a>
     */
    private void reconnect() {
        if (state != State.CONNECT_ERROR) // connection not closed !!
            return;

        if (realWebSocket != null) {
            // Cancel websocket connection
            realWebSocket.cancel();
            // Clear websocket object
            realWebSocket = null;
        }

//        // Calculate delay time
//        int collision = reconnectionAttempts > MAX_COLLISION ? MAX_COLLISION : reconnectionAttempts;
//        long delayTime = Math.round((Math.pow(2, collision) - 1) / 2) * 1000;

        if(reconnectionAttempts != MAX_COLLISION) {

            changeState(State.RECONNECT_ATTEMPT);

            if (eventListener.get(EVENT_RECONNECT_ATTEMPT) != null) {
                eventListener.get(EVENT_RECONNECT_ATTEMPT).onMessage(MySocket.this, EVENT_RECONNECT_ATTEMPT);
            }

            // Remove any pending posts of callbacks
            delayedReconnection.removeCallbacksAndMessages(null);
            // Start new post delay
            delayedReconnection.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeState(State.RECONNECTING);
                    reconnectionAttempts++; // Increment connections attempts
                    connect(); // Establish new connection
                }
            }, 5000);
        }
    }

    private WebSocketListener webSocketListener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            Log.v(TAG, "Socket has been opened successfully.");
            // reset connections attempts counter
            reconnectionAttempts = 0;

            // fire open event listener
            if (eventListener.get(EVENT_OPEN) != null) {
                eventListener.get(EVENT_OPEN).onMessage(MySocket.this, EVENT_OPEN);
            }

            // Send data in queue
            for (String event : onOpenMessageQueue.keySet()) {
                send(event, onOpenMessageQueue.get(event));
            }
            // clear queue
            onOpenMessageQueue.clear();

            changeState(State.OPEN);
        }

        /**
         * Accept only Json data with format:
         * <b> {"event":"event name","data":{some data ...}} </b>
         */
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            // print received message in log
            Timber.v( "New Message received %s", text);

            // call message listener
            if (messageListener != null)
                messageListener.onMessage(MySocket.this, text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            // TODO: some action
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Log.v(TAG, "Close request from server with reason '" + reason + "'");
            changeState(State.CLOSING);
            webSocket.close(1000, reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.v(TAG, "Socket connection closed with reason '" + reason + "'");
            changeState(State.CLOSED);
            if (eventListener.get(EVENT_CLOSED) != null) {
                eventListener.get(EVENT_CLOSED).onMessage(MySocket.this, EVENT_CLOSED);
            }
        }

        /**
         * This method call if:
         * - Fail to verify websocket GET request  => Throwable {@link ProtocolException}
         * - Can't establish websocket connection after upgrade GET request => response null, Throwable {@link Exception}
         * - First GET request had been failed => response null, Throwable {@link java.io.IOException}
         * - Fail to send Ping => response null, Throwable {@link java.io.IOException}
         * - Fail to send data frame => response null, Throwable {@link java.io.IOException}
         * - Fail to read data frame => response null, Throwable {@link java.io.IOException}
         */
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            if (!skipOnFailure) {
                skipOnFailure = false; // reset flag
                Log.v(TAG, "Socket connection fail, try to reconnect. (" + reconnectionAttempts + ")");
                changeState(State.CONNECT_ERROR);
                reconnect();
            }
        }
    };

    public abstract static class OnMessageListener {
        public abstract void onMessage(String data);

        /**
         * Method called from socket to execute listener implemented in
         * {@link #onMessage(String)} on main thread
         *
         * @param socket Socket that receive the message
         * @param data   Data string received
         */
        private void onMessage(MySocket socket, final String data) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onMessage(data);
                }
            });
        }
    }

    public abstract static class OnEventListener {
        public abstract void onMessage(String event);

        private void onMessage(MySocket socket, final String event) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onMessage(event);
                }
            });
        }
    }

    public abstract static class OnEventResponseListener extends OnEventListener {
        /**
         * Method need to override in listener usage
         */
        public abstract void onMessage(String event, String data);

        /**
         * Just override the inherited method
         */
        @Override
        public void onMessage(String event) {
        }

        /**
         * Method called from socket to execute listener implemented in
         * {@link #onMessage(String, String)} on main thread
         *
         * @param socket Socket that receive the message
         * @param event  Message received event
         * @param data   Data received in the message
         */
        private void onMessage(MySocket socket, final String event, final JsonObject data) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onMessage(event, data.toString());
                    onMessage(event);
                }
            });
        }
    }

    public abstract static class OnStateChangeListener {
        /**
         * Method need to override in listener usage
         */
        public abstract void onChange(State status);

        /**
         * Method called from socket to execute listener implemented in
         * {@link #onChange(State)} on main thread
         *
         * @param socket Socket that receive the message
         * @param status new status
         */
        private void onChange(MySocket socket, final State status) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onChange(status);
                }
            });
        }
    }
}