package com.bestnewsapp.sendnewsdata.util;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Saurabh on 18-Mar-17.
 */

public class ParametricRequest extends Request<String> {

    private Map<String, String> mParams;

    public ParametricRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);

        mParams = new HashMap<String, String>();
        mParams.put("Content-Type", "");
        mParams.put("paramTwo", "");

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(String response) {

    }
}
