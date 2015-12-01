package com.mcts.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mcts.app.R;
import com.mcts.app.db.DBConstant;
import com.mcts.app.db.DatabaseHelper;
import com.mcts.app.utils.Constants;
import com.mcts.app.utils.Utils;

import java.io.File;
import java.io.IOException;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Activity thisActivity;
    private static String TAG="MainMenuActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private LinearLayout ll_first,ll_data_entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setToolBar();
        init();

        Constants.APP_STORAGE_DIRECTORY= Environment.getExternalStorageDirectory();
        Constants.DATABASE_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.DATABASE_DIRECTORY_NAME).append(File.separator).toString();
        Constants.IMAGES_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.IMAGES_DIRECTORY_NAME).append(File.separator).toString();
        Constants.VIDEOS_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.VIDEOS_DIRECTORY_NAME).append(File.separator).toString();
        Constants.AUDIOS_DIRECTORY_PATH = new StringBuilder(String.valueOf(Constants.APP_STORAGE_DIRECTORY.getAbsolutePath())).append(File.separator).append(Constants.AUDIOS_DIRECTORY_NAME).append(File.separator).toString();
        setUpDirectories();
    }

    private void setToolBar() {

        thisActivity = MainMenuActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.appTitle));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init(){

        DBConstant.DATABASE_DIR = Constants.DATABASE_DIRECTORY_PATH + File.separator;
        DatabaseHelper databaseHelper=new DatabaseHelper(thisActivity);
        try {
            databaseHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ll_first=(LinearLayout)findViewById(R.id.ll_first);
        Utils.findAllTextView(thisActivity, (ViewGroup) findViewById(R.id.ll_first));

        ll_data_entry=(LinearLayout)findViewById(R.id.ll_data_entry);
        ll_data_entry.setOnClickListener(this);
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
//        getMenuInflater().inflate(R.menu.menu_first, menu);
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

        switch (v.getId()){
            case R.id.ll_data_entry:
                Intent intent=new Intent(thisActivity,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
