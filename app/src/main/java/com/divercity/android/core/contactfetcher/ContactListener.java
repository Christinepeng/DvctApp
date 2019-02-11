package com.divercity.android.core.contactfetcher;

public interface ContactListener<T> {
    void onNext(T t);

    void onError(Throwable error);

    void onComplete();
}
