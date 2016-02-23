package com.mcts.app.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import org.json.JSONObject;

/**
 * Created by priteshpatel on 14-Aug-15.
 */
public class CommonClass {


    public static void hideSoftKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public static boolean isConnectingToInternet(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }
        }
        return false;
    }

    public static void addFragment(Context context, Fragment fragment,
                                   boolean addToBackStack, int transition, int layoutId) {

//        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        FragmentTransaction ft =((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        ft.replace(layoutId, fragment, "MY_FRAGMENT");
        ft.setTransition(transition);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    /**This method use for PostConnection to Server
     */
    public static String PostConnection(String strUrl,JSONObject jsonObject) {


        return null;
    }
}
