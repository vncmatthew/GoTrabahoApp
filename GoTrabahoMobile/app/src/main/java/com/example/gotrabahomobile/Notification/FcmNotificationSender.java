package com.example.gotrabahomobile.Notification;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {

    private final String userFcmToken;

    private final String title;
    private final String body;
    private final Context context;
    private final String postUrl = "https://fcm.googleapis.com/v1/gotrabahochat/messages:send";


    public FcmNotificationSender(String userFcmToken, String title, String body, Context context) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.context = context;
    }

    public void SendNotifications(){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JSONObject mainObj = new JSONObject();
        try{
            JSONObject messageObjext =new JSONObject();
            JSONObject notificationObject = new JSONObject();
            notificationObject.put("title", title);
            notificationObject.put("body", body);
            notificationObject.put("token", userFcmToken);
            notificationObject.put("notification", notificationObject);
            mainObj.put("message", messageObjext);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, response ->{

            }, volleyError -> {

            }){
                @NonNull
                @Override
                public Map<String, String> getHeaders(){
                    AccessToken accessToken = new AccessToken();
                    String accessKey = accessToken.getAccessToken();
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "Bearer " + accessKey);
                    return header;
                }
            };

            requestQueue.add(request);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }
}
