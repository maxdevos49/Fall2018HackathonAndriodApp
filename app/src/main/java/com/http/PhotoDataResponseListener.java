package com.http;

import org.json.JSONArray;

public interface PhotoDataResponseListener {

    void response(JSONArray obj);
    void failure(String message);

}
