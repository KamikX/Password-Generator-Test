package com.sarriel.pass;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import com.sarriel.pass.service.PasswordGeneratorService;

/**
 * Created by kdonoval on 17.11.2016.
 */

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    /**
     * Instance of Application
     */
    private static App instance;
    /**
     * Instance of Password Generator Service
     */
    private PasswordGeneratorService mService;
    /**
     * Flag, used for bound detection
     */
    private boolean mBound = false;


    /**
     *  Callbacks for service binding
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService  = ((PasswordGeneratorService.PasswordGeneratorBinder) service).getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // register for Activity lifecycle callbacks
        registerActivityLifecycleCallbacks(this);
        instance = this;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        if (activity instanceof MainActivity) {
            // Bind to Service
            Intent intent = new Intent(this, PasswordGeneratorService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {

        if (activity instanceof MainActivity) {
            // Unbind from the service only when activity is destroyed,
            // no when is invisible because password is generated even on background (Direct reply from notification)
            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }
        }
    }

    /**
     *  Get instance
     * @return
     */
    public static App getInstance(){
        return instance;
    }

    /***
     * Get service
     * @return
     */
    public PasswordGeneratorService getService()
    {
        return mService;
    }
}


