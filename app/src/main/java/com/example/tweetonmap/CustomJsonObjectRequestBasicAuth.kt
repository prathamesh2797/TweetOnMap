package com.example.tweetonmap

import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.tweetonmap.Constants.Companion.API_KEY
import org.json.JSONObject

class CustomJsonObjectRequestBasicAuth(method: Int, url: String, jsonObject: JSONObject?,
                                       listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener)
    : JsonObjectRequest(method,url,jsonObject,listener,errorListener) {
    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers.put("Authorization", "Bearer " + API_KEY);
        return headers;
    }
}