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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String saveImageWithName(Bitmap finalBitmap,String Name,String rootDir,String subDir) {

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



        //String fname = "Image-"+ n +".jpg";
        File file = new File(myDir, Name);
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

    /**
     * returning image / video
     */
    public static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.MCTS);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(Constants.MCTS, "Oops! Failed create "
                        + Constants.PROFILE_PIC + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile = null;
        if (type == Constants.MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }

        return mediaFile;
    }

    public static boolean checkAfterDate(Date ancServiceDate, Date regDate) {
        if (ancServiceDate != null && regDate != null) {
            if (ancServiceDate.after(regDate)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean checkBeforeDate(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static boolean between(Date date, Date dateStart, Date dateEnd) {
        if (date != null && dateStart != null && dateEnd != null) {
            if (date.after(dateStart) && date.before(dateEnd)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String addDays(String date,int days){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpleDateFormat.parse(date));
            c.add(Calendar.DATE, days);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(c.getTime());
    }

    public static int getYear(Date date1,Date date2){
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        Integer.parseInt(simpleDateformat.format(date1));

        return Integer.parseInt(simpleDateformat.format(date2))- Integer.parseInt(simpleDateformat.format(date1));

    }
}
