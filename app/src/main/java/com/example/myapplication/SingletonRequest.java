package com.example.myapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

class SingletonRequest {
    private static RequestQueue ourInstance;

    public synchronized static RequestQueue getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = Volley.newRequestQueue(context);
        }

        return ourInstance;
    }
}
