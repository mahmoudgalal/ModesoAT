package com.example.belalmohamed.modesoat;

import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by user on 24/12/2015.
 */
public class Utils {
    public static  class Constants
    {
        public static final String SIGNEDUP_KEY ="UserSignedupKey";
        public static  final String LAST_LOGIN_TIME ="UserLastLoginTime";
        public static  final int SIGNUP_FRAGMENT =0;
        public static  final int LOGIN_FRAGMENT =1;
        public static  final int START_FRAGMENT =2;
        public static  final long LOGOUT_PERIOD =1000*60*20;

    }
    public static void parseUserLogin(String userName,String userPass,LogInCallback callback)
    {
        ParseUser.logInInBackground(userName, userPass, callback);
    }
    public static void subscribeParseChannel(String channel,SaveCallback callback)
    {
        ParsePush.subscribeInBackground(channel, callback);
    }
    public static void unSubscribeParseChannel(String channel,SaveCallback callback)
    {
        if(callback!= null)
        ParsePush.unsubscribeInBackground(channel, callback);
        else
            ParsePush.unsubscribeInBackground(channel);
    }
}
