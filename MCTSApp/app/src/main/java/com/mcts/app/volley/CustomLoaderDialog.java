package com.mcts.app.volley;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.utils.Messages;


public class CustomLoaderDialog {
	Context c;
	Dialog progressDialog;
	Typeface fontLight=null;

	public CustomLoaderDialog(Context context) {
		this.c = context;
	}
/**
 * This method use for show progress dailog
 * @param isCancelable set true if you set calcel progressDialog by user event
 */
	public void show(Boolean isCancelable) {
		progressDialog = new Dialog(c, R.style.DialogTheme);
		LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View progressView = mInflater.inflate(R.layout.custom_progress_dialog, null);
		TextView txt_protext = (TextView) progressView.findViewById(R.id.txt_protext);
		txt_protext.setText(R.string.please_wait);
		Typeface type = Typeface.createFromAsset(c.getAssets(), "SHRUTI.TTF");
		txt_protext.setTypeface(type);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		progressDialog.setContentView(progressView);

		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
		Display display = wm.getDefaultDisplay(); // getting the screen size of device
		Point size = new Point();
		display.getSize(size);
		int width1 = WindowManager.LayoutParams.WRAP_CONTENT;
		int height1 = WindowManager.LayoutParams.WRAP_CONTENT;

		int tempValue = 0;
		tempValue = ((size.x) * 200) / 1440;
		int width = size.x - tempValue;  // Set your widths
		int height = height1; // set your heights

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(progressDialog.getWindow().getAttributes());

		lp.width = width;
		lp.height = height;
		progressDialog.getWindow().setAttributes(lp);
		progressDialog.setContentView(progressView);
		progressDialog.setCancelable(isCancelable);
		progressDialog.show();
	}

	public void showValidationDialog(String validationString) {
		progressDialog = new Dialog(c, R.style.DialogTheme);
		LayoutInflater mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View progressView = mInflater.inflate(R.layout.custom_validation_dialog, null);
		TextView txt_validation = (TextView) progressView.findViewById(R.id.txt_validation);
		TextView txt_title = (TextView) progressView.findViewById(R.id.txt_title);
		Button bt_yes = (Button) progressView.findViewById(R.id.bt_yes);
		txt_validation.setText(validationString);
		Typeface type = Typeface.createFromAsset(c.getAssets(), "SHRUTI.TTF");
		txt_validation.setTypeface(type);
		txt_title.setTypeface(type,Typeface.BOLD);
		bt_yes.setTypeface(type);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		progressDialog.setContentView(progressView);

		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE); // for activity use context instead of getActivity()
		Display display = wm.getDefaultDisplay(); // getting the screen size of device
		Point size = new Point();
		display.getSize(size);
		int width1 = WindowManager.LayoutParams.WRAP_CONTENT;
		int height1 = WindowManager.LayoutParams.WRAP_CONTENT;

		int tempValue = 0;
		tempValue = ((size.x) * 200) / 1440;
		int width = size.x - tempValue;  // Set your widths
		int height = height1; // set your heights

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(progressDialog.getWindow().getAttributes());

		lp.width = width;
		lp.height = height;
		progressDialog.getWindow().setAttributes(lp);
		progressDialog.setContentView(progressView);
		progressDialog.setCancelable(false);
		progressDialog.show();

		bt_yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				progressDialog.dismiss();
			}
		});

	}

	/*public View showAlert(View view1) {

		dialog = new Dialog(c, android.R.style.Animation_Dialog);
		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(130);
		dialog.getWindow().setBackgroundDrawable(d);
		dialog.setCancelable(true);

		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.alertdialog, null);

		LinearLayout ln = (LinearLayout) view.findViewById(R.id.laymsgbox);
		ln.addView(view1);

		LinearLayout btnOkPopup = (LinearLayout) view.findViewById(R.id.btnOkPopup);
		btnOkPopup.setVisibility(View.GONE);

		dialog.setContentView(view);
		dialog.show();
		return view1;
	}*/
	/*public void showAlert(String msg) {
		fontLight = Typeface.createFromAsset(this.c.getAssets(),"fonts/Helvetica Neue Light_1.ttf");
		
		hide();
		dialog = new Dialog(c, android.R.style.Animation_Dialog);
		Drawable d = new ColorDrawable(Color.BLACK);
		d.setAlpha(130);
		dialog.getWindow().setBackgroundDrawable(d);
		dialog.setCancelable(true);

		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.alertdialog, null);

		Button btnOkPopup=(Button)view.findViewById(R.id.btnOkPopup);
		btnOkPopup.setTypeface(fontLight);
		
		TextView txtV = new TextView(c);
		txtV.setTypeface(fontLight);
		txtV.setText(msg);
		txtV.setGravity(Gravity.CENTER_HORIZONTAL);		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		txtV.setLayoutParams(lp);
		txtV.setTextAppearance(this.c, R.style.PopupTextView);
		LinearLayout ln = (LinearLayout) view.findViewById(R.id.laymsgbox);
		ln.addView(txtV);

		dialog.setContentView(view);
		dialog.show();
	}*/
	public void hide() {
		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog.dismiss();
		}
	}
}
