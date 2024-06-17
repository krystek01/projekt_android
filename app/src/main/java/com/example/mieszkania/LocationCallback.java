package com.example.mieszkania;

public interface LocationCallback {
    void onLocationFound(String city);
    void onError(String error);
}

