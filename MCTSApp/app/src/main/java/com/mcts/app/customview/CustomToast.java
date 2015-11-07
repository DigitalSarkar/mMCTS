package com.mcts.app.customview;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcts.app.R;

public class CustomToast {
    private Activity a;
//    private int b;
    private String c;

    public CustomToast(Activity activity, String str) {
        this.a = activity;
        this.c = str;
    }

    public void show() {
        LayoutInflater inflater = a.getLayoutInflater();
        View convertView = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView textView = (TextView) convertView.findViewById(R.id.txt_toast);
        textView.setText(c);

        Typeface type = Typeface.createFromAsset(a.getAssets(), "SHRUTI.TTF");
        textView.setTypeface(type,Typeface.BOLD);
        Toast toast = new Toast(this.a);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(convertView);
        toast.show();
    }
}
