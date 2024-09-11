package com.example.gotrabahomobile.Notification;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FcmNotificationSender {

    private final String userFcmToken;
    private final String jsonString = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"gotrabahochat\",\n" +
            "  \"private_key_id\": \"6771590922b5b01be8d7299db9ed87ff7a9c3880\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC4is3+nMityDtp\\nvghfcjw1aeRrR95l1zLArg7a2ZQFE1d/kyAtpkELQNkZnFRSNJ+9jFxewJEfAKLV\\nZUvTNAnl/ccn4zRQ9nWkvNhkSf0eWzfpDUhPUz/UBlgJlFppvey2QAiaQ4xHmp6r\\nidmza8MZ6YG65xTAGmfkaG/P/RU3sjDhzgky+WO8E666LkDBpHDBvWo+WWO4D2OM\\nd+9LH6QrCrH0Vg+aNnbzKYmuGlPc+4oyG+EioHd8FDKlUpTQpuzAiTn+Ke+t14wu\\nOiAlUzrgOttL2J2bBlQQ1uLHhIyBQdPTZicq5gNNHDv1Mz4vGcSaqyCTpbexhZDj\\nFsr57eMrAgMBAAECggEADdt1GBs7dyUzz5Rn+jbQmO2F1p0ecO+bDYc5xqro56//\\nStfy5h/3qWR/Xjd0HiTPy+1yxYtYj7HosvUjdPgRjYt5aCAyU6lgxckycebGaCr5\\nr06ChrEIeB+D09r9JmHkblOvnFnZz59SLoJa7mih+AZQlLF5HZW+Bb474IEvay+d\\ntu4YPzf2oUSVz59tDBU7xznmVQBTCuQusys4tnoV2E9dJRVOu0IHylZAmA9BTLpI\\nxwCXw/38Ky6V0WT6cYqFGn4Mimc1SYhgcgKvm0pTKq637ngPvg4afTBilw2HhoaL\\nQHQ1dhKAMbruzM2pzaH21qsoA5SK1jutc3bMBkSlFQKBgQDkwpzQPbUTZj2SpG7X\\n9kMmeg0Vw9bGOoLAQncUOKsgMIOyER49Z4OFlcYutPhDSjyfaxkD36RIN7ag21+4\\nfQKystV8cijTi2PtjA5rYoSoBSIim/uC1L732cfgyHM/fn/UT+awuyEKNxCqE254\\nVMyrbWv2j+L6TCArOGJgLH2dNwKBgQDOhEcEtqiFb5V2O9lMQsuMcXFtB0/icwVa\\nx9pZgWhIYKqySO28lItEKW0n9bdcf2GXtlY4rI5B83GNzTyOQIjfvLdRxbDBO+7Z\\nE0pbc8QM67tN+aMpEvJ6cxix7o26obyOZVh4IUXfsmlIWz8RF9yfn+q1Yb2sE4zc\\nuZh7ouUDrQKBgQDIz7XMb37XCrceVU4gMkN8MUPvSTzuQkqo7y+NBrnhxfS1iaVc\\nDdQ8nd+c/Ku6lh5layRIbDfQVWfyCOEcGFEYATi6x5PjL8eGrZ2y0obU37y5WOgO\\nTydcd0aoog3ZPPiFraY6mepFB1WPlwMwinAoRCO0C1calVzf0903w5Z5PQKBgAYl\\nDW/od28w+MOB2F9ahW/yWCukUg4s2fXviGGtZgrxRWL0pI495T7r9KGBwCjyaVb4\\nR+x7xefUdIaBmtGovenaVaILPc0iGWNAHbBG64hu4y3YxbANLkXScuOoc3MgKNob\\nzdwVMV0Mk/FhNakuipP9UCCBfgFGu6Q9qXnUSPgpAoGAPT7UNVTQxl+loq6yC8Vx\\njx0PxJ0PmquYW/rHRiUOAOEuhnbLx+uQY0HOCmP86ESrpQJr5cNJ5p6PIYMKsu3B\\nrdcuGdl+VvfCn9FhmeSG4BnrRbBq/kIPwWToiRidoaGaUgMsoNHbUWGY1coHGdu+\\n/LSx2zrJRUXuKxSbaSE1WY0=\\n-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"firebase-adminsdk-8e1ol@gotrabahochat.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"104721263073415545542\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-8e1ol%40gotrabahochat.iam.gserviceaccount.com\",\n" +
            "  \"universe_domain\": \"googleapis.com\"\n" +
            "}\n";
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
                    AccessToken accessToken = null;
                    try {
                        accessToken = new AccessToken(jsonString);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
