package co.com.une.appmovilesune.controller;

import com.google.analytics.tracking.android.EasyTracker;

import co.com.une.appmovilesune.MainActivity;
import co.com.une.appmovilesune.R;

import android.app.Activity;
import android.os.Bundle;

public class ControlObsequio extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewobsequios);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.seguimiento) {
            EasyTracker.getInstance(this).activityStart(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (MainActivity.seguimiento) {
            EasyTracker.getInstance(this).activityStop(this);
        }
    }

}
