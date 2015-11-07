package com.mcts.app.volley;

import android.app.Application;

import com.mcts.app.utils.MyVolley;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant extends Application {

	//public static String  WEBSERVICE_URL ="http://moozedesign.us/ver2/";
	public static String WEBSERVICE_URL = "http://private-ca766-vorfo.apiary-mock.com/";

	public static String USER_IMAGE_URL = "";
	public static String ADD_IMAGE_URL = "";


	public static final String POST_REQUEST = "POST";
	public static final String GET_REQUEST = "GET";

	/**
	 * VOLLEY_RETRY_COUNT set count no. of call web mthod with volley by Default is 1.
	 */
	public static final String METHOD_FOR_SEND_ATCATS = "";
	public static final int VOLLEY_RETRY_COUNT = 1;


	/**
	 * CALL_TIME_OUT set webservice calling time out
	 */
	public static final int CALL_TIME_OUT = 50000;

	public static final int CALL_CONTACTS_TIME_OUT = 480000;
	/**
	 * @param shouldCache   webapi response are stored in catch(true) or not(false)
	 */
	public static final Boolean SHOULD_CACHE = false;

	/**
	 * @param IS_PROGRESSDAILOG_SHOW  set true if you set progressDialog by default true
	 */
	public static final Boolean IS_PROGRESSDAILOG_SHOW = true;
	/**
	 * @param IS_PROGRESSDAILOG_CANCELABLE  set true if you set cancel progressDialog by user event
	 */
	public static final Boolean IS_PROGRESSDAILOG_CANCELABLE = false;

	/**
	 * Server Id for Get Device token , get server id is project id from google console 
	 * */


	/**
	 * Time for showing arc menu
	 */
	public static int MENU_DEFAULT_TIME = 400;

	public static boolean is_invite = false;

	/**
	 * for forground notification actions
	 * caller screen
	 */

	public interface ACTION {


	}

	public interface NOTIFICATION_ID {
	}


	@Override
	public void onCreate() {
		super.onCreate();

		init();
	}


	private void init() {
		MyVolley.init(this);
	}
}
