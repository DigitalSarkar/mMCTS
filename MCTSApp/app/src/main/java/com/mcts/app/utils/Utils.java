package com.mcts.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

/**
 * Created by Raj on 10/12/2015.
 */
public class Utils {

    public static void findAllTextView(Context context,ViewGroup viewGroup) {
        Typeface type = Typeface.createFromAsset(context.getAssets(), "SHRUTI.TTF");
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                findAllTextView(context,(ViewGroup) view);
            else if (view instanceof TextView) {
                TextView textView = (TextView) view;

                textView.setTypeface(type, Typeface.BOLD);
            }
        }

    }

    public static String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }

    public static String getIMEINumber(Context context){
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tMgr.getDeviceId();
        return IMEI;
    }


    public static void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static void ButtonClickEffect(final View v)
    {
        AlphaAnimation obja = new AlphaAnimation(1.0f, 0.3f);
        obja.setDuration(5);
        obja.setFillAfter(false);
        v.startAnimation(obja);

    }
}
