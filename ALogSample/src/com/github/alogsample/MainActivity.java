package com.github.alogsample;

import com.github.alog.ALog;
import com.github.alog.ALogger;
import com.github.alog.ALogger.ALoggerFactory;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    
    private static String TAG = MainActivity.class.getSimpleName();
    private static ALogger LOG = ALoggerFactory.getLogger(TAG);
    private static boolean D = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (D) ALog.i(LOG, TAG, "onCreate()");
    }
}
