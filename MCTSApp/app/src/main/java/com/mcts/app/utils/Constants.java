package com.mcts.app.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Constants {

    public static File APP_STORAGE_DIRECTORY = null;

    public static String AUDIOS_DIRECTORY_NAME = null;
    public static String AUDIOS_DIRECTORY_PATH = null;
    public static final String DATABASE_DIRECTORY_NAME = "Databse";
    public static String DATABASE_DIRECTORY_PATH = null;
    public static String IMAGES_DIRECTORY_NAME = null;
    public static String IMAGES_DIRECTORY_PATH = null;
    public static String VIDEOS_DIRECTORY_NAME = null;
    public static String VIDEOS_DIRECTORY_PATH = null;
    public static final String USER_LOGIN_PREF="user";
    public static final String USER_ID="userId";

    public static final String BASE_URL="http://digitalrecord.in/Webservice.asmx/";
    public static final String MEMBER_DETAIL="memberdetails";
    public static final String FAMILY_DETAIL="familydetails";
    public static final String USER_LOGIN="userlogin";
    public static final String VILLAGE="villagedetails";

// web service call array
    public static String[] webApiArrayList=new String[]{
        MEMBER_DETAIL,
        FAMILY_DETAIL,
        VILLAGE
    };

    static {
        DATABASE_DIRECTORY_PATH = null;
        IMAGES_DIRECTORY_PATH = null;
        IMAGES_DIRECTORY_NAME = "Images";
        VIDEOS_DIRECTORY_PATH = null;
        VIDEOS_DIRECTORY_NAME = "Videos";
        AUDIOS_DIRECTORY_PATH = null;
        AUDIOS_DIRECTORY_NAME = "Audios";
    }


}
