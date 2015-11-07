package com.mcts.app.volley;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mcts.app.R;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.MyVolley;
import com.mcts.app.utils.NetworkUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BaseActivity extends AppCompatActivity implements IVolleyRespose {

	/*//---------------------call for volley service------------------------------------------------------------------
	public RequestQueue queue;
	StringRequest jsStrRequest,addRequest;
	//JsonObjectRequest j
	String res=null;
	CustomLoaderDialog cm;
	public static final int RESPONSE_OK=200; 
	public static final int RESPONSE_ERROR=404; 
	//---------------------call for volley service------------------------------------------------------------------
*/
	
	//---------------------call for volley service------------------------------------------------------------------
		public RequestQueue queue;
	CustomLoaderDialog cm;
		StringRequest jsStrRequest;
	//	JsonObjectRequest jsStrRequest;
		//JsonObjectRequest j
		String res=null;
		public static final int RESPONSE_OK=200; 
		public static final int RESPONSE_ERROR=404; 
		//---------------------call for volley service------------------------------------------------------------------

		
	BaseActivity mCommonActivity;
	
	ProgressDialog progressDialog;

	public void printToastAlert(Context context,int id)
	{
		Toast.makeText(context, ""+context.getResources().getString(id), Toast.LENGTH_SHORT).show();
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		//  String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		String expression ="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/*public boolean isOnline(Context cContext) {
		ConnectivityManager cm =
				(ConnectivityManager)cContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}*/

	public void printToastShort(Context context,String string)
	{
		Toast.makeText(context, ""+string, Toast.LENGTH_SHORT).show();
	}

	public String prepareWebserviceRequest(String methodname, String[] keys, String[] values) throws JSONException{
		String retStr = null;
		String strParams = null;
		JSONObject json = new JSONObject();
		for(int i=0;i<keys.length;i++){
			json.put(keys[i],values[i]);
		}

		JSONObject fJson = new JSONObject();
		fJson.put("method_name", methodname);
		fJson.put("body", json);
		retStr = fJson.toString();
		return retStr;
	}

	public String prepareWebserviceRequestforvolly(String methodname, String[] keys, String[] values) throws JSONException{
		String retStr = null;
		//String strParams = null;
		JSONObject json = new JSONObject();

		if(keys!=null)
		{
			for(int i=0;i<keys.length;i++){
				json.put(keys[i],values[i]);
			}
		}
		JSONObject fJson = new JSONObject();
		fJson.put("method_name", methodname);
		if(keys!=null)
			fJson.put("body", json);
		retStr = fJson.toString();
		return retStr;
	}


	/////////////////////////conver dp to pixl and viseversa
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}


	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public static void ButtonClickEffect(View v)
	{
		AlphaAnimation obja = new AlphaAnimation(1.0f, 0.3f);
		obja.setDuration(10);
		obja.setFillAfter(false);
		v.startAnimation(obja);
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	// check tablet or phone method..................................
	public boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
		//..................................
	}


	public void printAlert(Context context)
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(""+getResources().getString(R.string.app_name));
		//alert.setMessage(""+getResources().getString(R.string.blockuser));
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) { 


			}});
		alert.create();alert.show();
	}

	//---------------------call for volley service------------------------------------------------------------------
	/**
	 * <h1>  Use for calling volley webService </h1>
	 * 
	 *  @param  cContext           	Context of activity from where you call the webService
	 *  @param  mMethodType    		Should be POST or GET
	 *  @param  mMethodname   		Name of the method you want to call
	 *  @param  URL          		Url of your webService
	 *  @param  map           		Key Values pairs
	 *	@param  initialTimeoutMs 	Timeout of webService in milliseconds	   
	 *  @param  shouldCache   		Web Api response are stored in catch(true) or not(false)
	 *  @param  maxNumRetries 		maximum number in integer for retries to execute webService
	 *  @param 	isCancelable 		set true if you set cancel progressDialog by user event
	 * 	@param  aActivity 			pass your activity object
	 */
	public void callVolley(final Context cContext,String mMethodType,final String mMethodname,String URL, final Map map,int initialTimeoutMs,boolean shouldCache,int maxNumRetries,final Boolean isProgressDailogEnable,Boolean isCancelable,final BaseActivity aActivity){
		// TODO Auto-generated method stub
		Log.i("call","service");

			Log.i("call","2service");
			int reqType=0;
			String RequestURL=URL.trim();
			queue = MyVolley.getRequestQueue();

			if(mMethodType.trim().equalsIgnoreCase("GET"))
				reqType=com.android.volley.Request.Method.GET;
			else if(mMethodType.trim().equalsIgnoreCase("POST"))
				reqType=com.android.volley.Request.Method.POST;

			if (RequestURL.equals(null)) 
				RequestURL= Constants.BASE_URL;
			else
				RequestURL=URL;

			cm=new CustomLoaderDialog(cContext);
			cm.show(true);

			Log.v("RequestURL",RequestURL);

			jsStrRequest = new StringRequest(reqType,RequestURL, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					Log.i("response", "" + response);
					cm.hide();
					if (response == null) {
						IVolleyRespose iVolleyRespose= (IVolleyRespose) aActivity;
						iVolleyRespose.onVolleyResponse(RESPONSE_ERROR,response,mMethodname);
						res=response;
					}
					else if (response != null) {
						IVolleyRespose iVolleyRespose= (IVolleyRespose) aActivity;
						iVolleyRespose.onVolleyResponse(RESPONSE_OK,response,mMethodname);
						res=response;
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {
					// TODO Auto-generated method stub
					res="";
					cm.hide();
					IVolleyRespose iVolleyError =(IVolleyRespose) aActivity;
					iVolleyError.onVolleyError(RESPONSE_ERROR,"Error",mMethodname);
				}
			}){
				@Override
				protected Map<String,String> getParams(){
				/*	String  strRequest ="";
					try {
						strRequest=getWebservicejsObjRequestforvolley(mMethodname, jsonObject);
						strRequest=strRequest.replace(" ", "%20");
						Log.d("strRequest", ""+strRequest);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/

					Map<String,String> params = new HashMap<String, String>();
					params=map;
					return params;
				}
				/*@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String,String> params = new HashMap<String, String>();
					params.put("Content-Type","application/json");
					return params;
				}*/
			};
			jsStrRequest.setTag(mMethodname);
			jsStrRequest.setShouldCache(shouldCache);
			jsStrRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs,maxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			queue.add(jsStrRequest);

	}


	//---------------------call for volley service------------------------------------------------------------------
	/**
	 * <h1>  Use for calling volley webService </h1>
	 *
	 *  @param  cContext           	Context of activity from where you call the webService
	 *  @param  mMethodType    		Should be POST or GET
	 *  @param  mMethodname   		Name of the method you want to call
	 *  @param  URL          		Url of your webService
	 *  @param  map           		Key Values pairs
	 *	@param  initialTimeoutMs 	Timeout of webService in milliseconds
	 *  @param  shouldCache   		Web Api response are stored in catch(true) or not(false)
	 *  @param  maxNumRetries 		maximum number in integer for retries to execute webService
	 *  @param 	isCancelable 		set true if you set cancel progressDialog by user event
	 * 	@param  aActivity 			pass your activity object
	 */
	public void callVolleyWithoutParam(final Context cContext,String mMethodType,final String mMethodname,String URL, final Map map,int initialTimeoutMs,boolean shouldCache,int maxNumRetries,final Boolean isProgressDailogEnable,Boolean isCancelable,final BaseActivity aActivity){
		// TODO Auto-generated method stub
		Log.i("call","service");

		Log.i("call","2service");
		int reqType=0;
		String RequestURL=URL.trim();
		queue = MyVolley.getRequestQueue();

		if(mMethodType.trim().equalsIgnoreCase("GET"))
			reqType=com.android.volley.Request.Method.GET;
		else if(mMethodType.trim().equalsIgnoreCase("POST"))
			reqType=com.android.volley.Request.Method.POST;

		if (RequestURL.equals(null))
			RequestURL= Constants.BASE_URL;
		else
			RequestURL=URL;

		cm=new CustomLoaderDialog(cContext);
		cm.show(true);

		Log.v("RequestURL",RequestURL);

		jsStrRequest = new StringRequest(reqType,RequestURL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.i("response", "" + response);
				cm.hide();
				if (response == null) {
					IVolleyRespose iVolleyRespose= (IVolleyRespose) aActivity;
					iVolleyRespose.onVolleyResponse(RESPONSE_ERROR,response,mMethodname);
					res=response;
				}
				else if (response != null) {
					IVolleyRespose iVolleyRespose= (IVolleyRespose) aActivity;
					iVolleyRespose.onVolleyResponse(RESPONSE_OK,response,mMethodname);
					res=response;
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// TODO Auto-generated method stub
				res="";
				cm.hide();
				IVolleyRespose iVolleyError =(IVolleyRespose) aActivity;
				iVolleyError.onVolleyError(RESPONSE_ERROR,"Error",mMethodname);
			}
		}){

				/*@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String,String> params = new HashMap<String, String>();
					params.put("Content-Type","application/json");
					return params;
				}*/
		};
		jsStrRequest.setTag(mMethodname);
		jsStrRequest.setShouldCache(shouldCache);
		jsStrRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs,maxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(jsStrRequest);

	}

	//---------------------call for volley service------------------------------------------------------------------
	/**
	 * <h1>  Use for calling volley webService </h1>
	 * 
	 *  @param  cContext           	Context of activity from where you call the webService
	 *  @param  mMethodType    		Should be POST or GET
	 *  @param  mMethodname   		Name of the method you want to call
	 *  @param  URL          		Url of your webService
	 *   mMap          		Key Values pairs
	 *	@param  initialTimeoutMs 	Timeout of webService in milliseconds	   
	 *  @param  shouldCache   		Web Api response are stored in catch(true) or not(false)
	 *  @param  maxNumRetries 		maximum number in integer for retries to execute webService
	 *  @param 	isCancelable 		set true if you set cancel progressDialog by user event
	 * 	@param  aActivity 			pass your activity object
	 */
	/*public void callVolley(final Context cContext,String mMethodType,final String mMethodname,String URL, final JSONObject jsonObject,int initialTimeoutMs,boolean shouldCache,int maxNumRetries,final Boolean isProgressDailogEnable,Boolean isCancelable,final BaseActivity aActivity){
		// TODO Auto-generated method stub

		if (!MyUtility.checkInternetConnection(cContext)) {
			//showErrorDailog(aActivity, Constant.PleaseCheckInternetConnection,R.drawable.ic_launcher);
		}
		else{
			
			int reqType=0;
			String RequestURL=URL.trim();
			queue = Volley.newRequestQueue(cContext);

			if(mMethodType.trim().equalsIgnoreCase("GET"))
				reqType=com.android.volley.Request.Method.GET;
			else if(mMethodType.trim().equalsIgnoreCase("POST"))
				reqType=com.android.volley.Request.Method.POST;

			if (RequestURL.equals(null)) 
				RequestURL=Constant.WEBSERVICE_URL;
			else
				RequestURL=URL;

		
			Log.v("reqType",reqType+"");

			try {
				RequestURL=RequestURL+getWebservicejsObjRequestforvolley(mMethodname, jsonObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RequestURL=RequestURL.replace(" ", "%20");
			Log.v("RequestURL",RequestURL);
			
			jsStrRequest = new JsonObjectRequest(reqType, RequestURL, null, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					// TODO Auto-generated method stub
					Log.v("response", ""+response);
					if (response == null) {
						IVolleyRespose iVolleyRespose= (IVolleyRespose) aActivity;
						iVolleyRespose.onVolleyResponse(RESPONSE_ERROR,""+response,mMethodname);
						res=""+response;
					}
					else if (response != null) {
						IVolleyRespose iVolleyRespose= (IVolleyRespose) aActivity;
						iVolleyRespose.onVolleyResponse(RESPONSE_OK,""+response,mMethodname);
						res=""+response;
					}
				}
			}, new com.android.volley.Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {
					res="";
					
					IVolleyRespose iVolleyError =(IVolleyRespose) aActivity;
					iVolleyError.onVolleyError(RESPONSE_ERROR,"Error",mMethodname);
					

				}
			});
			jsStrRequest.setTag(mMethodname);
			jsStrRequest.setShouldCache(shouldCache);
			jsStrRequest.setRetryPolicy(new DefaultRetryPolicy(initialTimeoutMs,maxNumRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			queue.add(jsStrRequest);
		}
	}

	
	*/
	
	/**
	 * This method use for get String request for call web method
	 * mMethodname  Name of web method for call
	 * mMap 		   key value pair for preper json request
	 * */
	public String prepareReq(String methodName, HashMap<String, String> map){
		String final_request;
	    String param="";
	    if (map!=null && map.size()>0) {
	    	int length=map.size();
	    	 final_request=Constant.WEBSERVICE_URL+"?";	
	    	 int i=0;
	 	    for (Entry<String, String> entry : map.entrySet()) {
	 	     /**At last parameter '&' should not be add*/ 
	 	     if((i++)==(length-1))
	 	      param+=entry.getKey() + "=" + entry.getValue();
	 	     else
	 	      param+=entry.getKey() + "=" + entry.getValue() + "&";
	 	    }  
	 	    final_request+=param;
	    }
	    else{
	    	final_request=Constant.WEBSERVICE_URL+"?method_name="+methodName;
	    }
	    final_request=final_request.replace(" ", "%20");
	    return final_request;
	}
	
	
	
	/**
	 * This method use for get Json request for call web method
	 * @param mMethodname  Name of web method for call
	 * */
	@SuppressWarnings("deprecation")
	public String getWebservicejsObjRequestforvolley(String mMethodname, JSONObject json) throws JSONException{
		String retStr = null;

		/*JSONObject json = new JSONObject();

		if(mMap.size()>0){
			for (Map.Entry<String, String> e : mMap.entrySet()) {
				String key = e.getKey();
				String value =e.getValue();
				json.put(key,value);
			}
		}
		if(mMap!=null)*/
		if (json != null)
			retStr = URLEncoder.encode(json.toString());
		else
			retStr = "";
		
		return retStr;
	}

	@Override
	public void onVolleyResponse(int responseCode, String mRes,
			String ResponseTag) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVolleyError(int Code, String mError, String ResponseTag) {
		// TODO Auto-generated method stub

	}

	//---------------------call for volley service------------------------------------------------------------------

}





