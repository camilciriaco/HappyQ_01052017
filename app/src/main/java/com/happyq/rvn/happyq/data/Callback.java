package com.happyq.rvn.happyq.data;

public interface Callback<T> {

    void onSuccess(T result);

    void onError(String result);

}
