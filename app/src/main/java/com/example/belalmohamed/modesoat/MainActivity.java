package com.example.belalmohamed.modesoat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

import java.util.Date;

import fragments.LoginFragment;
import fragments.SignupFragment;
import fragments.WelcomeFragment;

/**
 * Created by belalmohamed on 12/23/15.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            long lastLoginTime = sp.getLong(Utils.Constants.LAST_LOGIN_TIME,-1);
            if(lastLoginTime<0)
            {
                String channel = (String) currentUser.get(SignupFragment.USER_CHANNEL_KEY);
                Utils.unSubscribeParseChannel(channel,null);
                ParseUser.logOut();
                loadFragment(Utils.Constants.LOGIN_FRAGMENT);
            }else {
                long currentTime = new Date().getTime();
                if((currentTime-lastLoginTime)>Utils.Constants.LOGOUT_PERIOD ) {
                    String channel = (String) currentUser.get(SignupFragment.USER_CHANNEL_KEY);
                    Utils.unSubscribeParseChannel(channel,null);
                    ParseUser.logOut();
                    loadFragment(Utils.Constants.LOGIN_FRAGMENT);
                }
                else
                loadFragment(Utils.Constants.START_FRAGMENT);
            }
        } else {
            // show the signup or login screen
            boolean signedup = sp.getBoolean(Utils.Constants.SIGNEDUP_KEY, false);
            if(!signedup)
                loadFragment(Utils.Constants.SIGNUP_FRAGMENT);
            else
                loadFragment(Utils.Constants.LOGIN_FRAGMENT);
        }

    }
    public void loadFragment(int index)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(index == Utils.Constants.SIGNUP_FRAGMENT)
        {
            SignupFragment signupFragment = new SignupFragment();
            Bundle args = new Bundle();
            //args.putSerializable("", "");
            signupFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, signupFragment, "SIGNUP_FRAGMENT")
                    .commit();
        }
        else if(index == Utils.Constants.LOGIN_FRAGMENT)
        {
            LoginFragment loginFragment = new LoginFragment();
            Bundle args = new Bundle();
            //args.putSerializable("", "");
            loginFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, loginFragment, "LOGIN_FRAGMENT")
                    .commit();
        }
        else if(index == Utils.Constants.START_FRAGMENT)
        {
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            Bundle args = new Bundle();
            //args.putSerializable("", "");
            welcomeFragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, welcomeFragment, "WELCOME_FRAGMENT")
                    .commit();
        }
    }
}
