package com.jf2mc1.a015004yuberclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.APP_ID))
                .clientKey(getResources().getString(R.string.CLIENT_KEY))
                .server(getResources().getString(R.string.URL))
                .build()
        );

    }

}
