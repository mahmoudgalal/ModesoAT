package com.example.belalmohamed.modesoat;

import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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

    /**
     * Sends a WOL(Wakeup-On-Lan) packet to the user PC with the specified MAC address
     * Note: the PC must be configured to support WOL
     * @param macAddress user's pc mac address
     */
    public static void sendWOLPacket(final String macAddress)
    {
        final int PORT = 9;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                String ipStr = "255.255.255.255";
                byte[] macBytes = new byte[6];
                String[] hex = macAddress.split("(\\:|\\-)");
                if (hex.length != 6) {
                    throw new IllegalArgumentException("Invalid MAC address.");
                }
                try {
                    for (int i = 0; i < 6; i++) {
                        macBytes[i] = (byte) Integer.parseInt(hex[i], 16);
                    }
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("Invalid hex digit in MAC address.");
                }


                try {
                    byte[] bytes = new byte[6 + 16 * macBytes.length];
                    for (int i = 0; i < 6; i++) {
                        bytes[i] = (byte) 0xff;
                    }
                    for (int i = 6; i < bytes.length; i += macBytes.length) {
                        System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                    }

                    InetAddress address = InetAddress.getByName(ipStr);
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);
                    socket.close();

                    System.out.println("Wake-on-LAN packet sent.");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Failed to send Wake-on-LAN packet: + e");
                    //System.exit(1);
                }
            }
        });
        t.start();


    }
}
