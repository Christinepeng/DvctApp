package com.divercity.app.core.ui;

import com.divercity.app.data.Status;

public class NetworkState {

    private Status status;

    private String message;

    private NetworkState(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    private NetworkState(Status status) {
        this.status = status;
    }

    public static NetworkState LOADED = new NetworkState(Status.SUCCESS);

    public static NetworkState LOADING = new NetworkState(Status.LOADING);

    public static NetworkState error(String message) {
        return new NetworkState(Status.ERROR, message == null ? "unknown error" : message);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
