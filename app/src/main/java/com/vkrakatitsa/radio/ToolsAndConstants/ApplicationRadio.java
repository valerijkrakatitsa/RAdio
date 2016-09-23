package com.vkrakatitsa.radio.ToolsAndConstants;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.vk.sdk.VKSdk;
import com.vkrakatitsa.radio.R;
import io.fabric.sdk.android.Fabric;

public class ApplicationRadio extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        VKSdk.initialize(getApplicationContext());
    }


}
