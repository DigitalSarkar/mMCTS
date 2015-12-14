package com.mcts.app.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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

    public static String saveIamge(Bitmap finalBitmap,String rootDir,String subDir) {

        File mediaStorageDir=null;
        if(rootDir!=null && rootDir.length()>0)
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),rootDir);
        else
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"TempImages");

        File myDir=null;
        if(subDir!=null && subDir.length()>0)
            myDir= new File(mediaStorageDir + "/" + subDir);
        else
            myDir= new File(mediaStorageDir+"");

//		deletFile(myDir);
//	    myDir.mkdirs();
        if(!myDir.exists())
            myDir.mkdirs();

        Random generator = new Random();
        int n = 100000;
        n = generator.nextInt(n);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String img_name="IMG-"+ n +".jpg";

        //String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, img_name);
        if (file.exists ())
            file.delete ();
        try {

            if(finalBitmap!=null)
            {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
//	    FILENAME = file.getName();
        return file.getPath();
    }
}
