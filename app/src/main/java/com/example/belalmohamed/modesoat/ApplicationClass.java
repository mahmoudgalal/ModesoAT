package com.example.belalmohamed.modesoat;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by belalmohamed on 12/23/15.
 */
public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "ucblrF3maptVHzVeB7aN8m3Nb1WtcknfXccvlvKE", "QJrIurABjfpB1TnRMuvPzVDpkmMobTXtYfV67Nz4");

        /*ParsePush.subscribeInBackground("BELALMOHAMED", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });*/
    }
}
