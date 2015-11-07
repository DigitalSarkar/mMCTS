package com.mcts.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.db.DBConstant;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Utils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Activity thisActivity;
    private static String TAG="MainActivity";
    private Toolbar mToolbar;
    private TextView mTitle,txt_startup_note,txt_yojana,txt_sansth_visit,txt_new_abhigam;
    Button bt_login,bt_today_schedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Constants.APP_STORAGE_DIRECTORY= Environment.getExternalStorageDirectory();
        Constants.DATABASE_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.DATABASE_DIRECTORY_NAME).append(File.separator).toString();
        Constants.IMAGES_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.IMAGES_DIRECTORY_NAME).append(File.separator).toString();
        Constants.VIDEOS_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.VIDEOS_DIRECTORY_NAME).append(File.separator).toString();
        Constants.AUDIOS_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.AUDIOS_DIRECTORY_NAME).append(File.separator).toString();
        setUpDirectories();
        init();

    }

    private void init() {

        thisActivity=MainActivity.this;
        DBConstant.DATABASE_DIR = Constants.DATABASE_DIRECTORY_PATH + File.separator;
        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        try {
            databaseHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Typeface type = Typeface.createFromAsset(getAssets(),"SHRUTI.TTF");


        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        setSupportActionBar(mToolbar);

        txt_startup_note = (TextView) findViewById(R.id.txt_startup_note);
        txt_yojana = (TextView) findViewById(R.id.txt_yojana);
        txt_sansth_visit = (TextView) findViewById(R.id.txt_sansth_visit);
        txt_new_abhigam = (TextView) findViewById(R.id.txt_new_abhigam);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_today_schedule = (Button) findViewById(R.id.bt_today_schedule);
        mTitle.setText(thisActivity.getResources().getString(R.string.appTitle));
        mTitle.setTypeface(type, Typeface.BOLD);
        txt_startup_note.setTypeface(type);
        bt_today_schedule.setTypeface(type,Typeface.BOLD);
        bt_login.setTypeface(type,Typeface.BOLD);
        txt_yojana.setTypeface(type, Typeface.BOLD);
        txt_sansth_visit.setTypeface(type, Typeface.BOLD);
        txt_new_abhigam.setTypeface(type, Typeface.BOLD);

        bt_login.setOnClickListener(this);
        bt_today_schedule.setOnClickListener(this);

    }

    public static void setUpDirectories() {
        File file = new File(Constants.IMAGES_DIRECTORY_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Constants.VIDEOS_DIRECTORY_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Constants.AUDIOS_DIRECTORY_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(Constants.DATABASE_DIRECTORY_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id){
            case R.id.bt_login:
                Intent intent=new Intent(thisActivity,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
