package com.example.dong.test814and;

/**
 * Created by dong on 2017/8/14.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}