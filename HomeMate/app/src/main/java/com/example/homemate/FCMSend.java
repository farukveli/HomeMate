package com.example.homemate;

import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class FCMSend {
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "key=AAAA2KBu6i0:APA91bFqB6rxVsFxPhvG6Tw30Nqjd-1fV1e9HGuqPi-h8uSxdWLC_bxUU9-MQwUZGMDh6E-x6snrsXK5dz--cih4Zs7kw-UAHQBOmQ-_f8PeyROE6pUbsjZCwTN1V0QcxqTByS-xaII1";

    public static void pushNotification(Context context, String token, String title, String message){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject json = new JSONObject();
            json.put("to",token);
            JSONObject notification = new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            json.put("notification",notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse response = error.networkResponse;
                    if(error instanceof ServerError && response!=null){
                        try{
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers,"utf-8"));
                            JSONObject obj = new JSONObject(res);
                        }catch (UnsupportedEncodingException e1){
                            e1.printStackTrace();
                        }catch (JSONException e2){
                            e2.printStackTrace();
                        }
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String ,String> params = new HashMap<>();
                    params.put("Content-Type","application/json; charset=utf-8");
                    params.put("Authorization",SERVER_KEY);
                    return params;
                }
            };
            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
