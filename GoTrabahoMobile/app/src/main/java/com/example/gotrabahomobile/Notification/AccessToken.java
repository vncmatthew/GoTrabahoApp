package com.example.gotrabahomobile.Notification;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccessToken {

    private static final String FIREBASE_MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private GoogleCredentials googleCredentials;

    public AccessToken(String jsonKeyString) throws IOException {
        InputStream stream = new ByteArrayInputStream(jsonKeyString.getBytes(StandardCharsets.UTF_8));
        googleCredentials = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList(FIREBASE_MESSAGING_SCOPE));
    }

    public String getAccessToken() {
        try {
            if (googleCredentials == null || !googleCredentials.hasRequestMetadata()) {
                throw new IllegalStateException("Google credentials are not initialized");
            }

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            Log.e("AccessTokenProvider", "Failed to obtain access token", e);
            throw new RuntimeException(e);
        }
    }

}
