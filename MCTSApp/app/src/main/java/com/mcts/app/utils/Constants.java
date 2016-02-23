package com.mcts.app.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Constants {

    public static File APP_STORAGE_DIRECTORY = null;
    public static String MCTS = "MCTS";
    public static final String APP_NAME = "MCTS";
    public static final String PROFILE_PIC = "Profile Pic";

    public static final int MEDIA_TYPE_IMAGE = 1;

    public static String AUDIOS_DIRECTORY_NAME = null;
    public static String AUDIOS_DIRECTORY_PATH = null;
    public static final String DATABASE_DIRECTORY_NAME = "mcts.sqlite";
    public static String DATABASE_DIRECTORY_PATH = null;
    public static String IMAGES_DIRECTORY_NAME = null;
    public static String IMAGES_DIRECTORY_PATH = null;
    public static String VIDEOS_DIRECTORY_NAME = null;
    public static String VIDEOS_DIRECTORY_PATH = null;
    public static final String USER_LOGIN_PREF = "user";
    public static final String USER_ID = "userId";
    public static final String DATA_DOWNLOAD = "download";
    public static final String DOWNLOAD = "1";
    public static final String DATA_OPERATION = "dataOperation";
    public static final String DATA_UPLOAD = "1";
    public static final String DATA_UPLOAD_DATE_TIME = "dateUploadTime";
    public static final String DATA_DOWNLOAD_DATE_TIME = "dateDownloadTime";

    /* GBase URL for Web APIs */
    public static final String BASE_URL = "http://careforall.in/Webservice.asmx/";

    /* Get Data from server to local*/
    public static final String MEMBER_DETAIL = "memberdetails";
    public static final String FAMILY_DETAIL = "familydetails";
    public static final String USER_LOGIN = "userlogin";
    public static final String VILLAGE = "villagedetails";
    public static final String AGANWADI = "anganwadi";
    public static final String EMPLOYEE_DETAILS = "employeedetails";
    public static final String CHILD_REGISTRATION_DETAILS = "childregistrationdetails";
    public static final String CHILD_IMMUNIZATION_DETAILS = "childimmunizationdetails";
    public static final String ADOPTED_METHOD_DETAILS = "adoptedfpmethoddetails";
    public static final String ANCSERVICE_DETAILS = "ancservicedetails";
    public static final String ANC_TOXOID_DETAILS = "anctetanustoxoiddetails";
    public static final String FALIYA_DETAILS = "faliyadetails";
    public static final String HIGH_RISK_DETAILS = "highriskmotherdetails";
    public static final String POSTNATAL_SERVICE_DETAILS = "postnatalServicedetails";
    public static final String PREGNANT_WOMAN_DETAILS = "pregnantwomanregddetails";
    public static final String PREGNANCY_OUTCOME_DETAILS = "pregnancyoutcomeregddetails";
    public static final String MEMBER_HISTORY_DETAILS = "memberhistorydetails";

    /* Post Data from local to server*/
    public static final String MEMBER_POST = "membermodify";
    public static final String MEMBER_HISTORY_POST = "memberhistorymodify";
    public static final String PREGNANT_WOMAN_POST = "pregnantwomanregdmodify";
    public static final String ANC_SERVICE_POST = "ancserviceamodify";
    public static final String HIGH_RISK_POST = "highriskmothermodify";
    public static final String ANC_TOXOID_POST = "anctetanustoxoidmodify";
    public static final String PREG_OUTCOME_POST = "pregnancyoutcomeregdmodify";
    public static final String FALIYA_POST = "faliyamodify";
    public static final String FAMILY_POST = "familymodify";
    public static final String CHILD_REG_POST = "childregistrationamodify";
    public static final String POSTNATAL_SERVICES_POST = "postnatalServicedmodify";
    public static final String CHILD_IMMUNIZATION_POST = "childimmunizationmodify";

    /* web service call array */
    public static String[] webApiArrayList = new String[]{
            MEMBER_DETAIL,
            FAMILY_DETAIL,
            VILLAGE,
            AGANWADI,
            EMPLOYEE_DETAILS,
            CHILD_REGISTRATION_DETAILS,
            CHILD_IMMUNIZATION_DETAILS,
            ADOPTED_METHOD_DETAILS,
            ANCSERVICE_DETAILS,
            ANC_TOXOID_DETAILS,
            HIGH_RISK_DETAILS,
            FALIYA_DETAILS,
            POSTNATAL_SERVICE_DETAILS,
            PREGNANT_WOMAN_DETAILS,
            PREGNANCY_OUTCOME_DETAILS,
            MEMBER_HISTORY_DETAILS

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
