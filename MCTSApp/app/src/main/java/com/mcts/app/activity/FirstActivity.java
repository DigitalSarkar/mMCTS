package com.mcts.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcts.app.R;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener{

    private Activity thisActivity;
    private static String TAG="FirstActivity";
    private Toolbar mToolbar;
    private TextView mTitle;
    private ImageView img_data_entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setToolBar();
        init();
    }

    private void setToolBar() {

        thisActivity = FirstActivity.this;
        Typeface type = Typeface.createFromAsset(getAssets(), "SHRUTI.TTF");
        mToolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        mTitle = (TextView) mToolbar.findViewById(R.id.title_text);
        mTitle.setText(thisActivity.getResources().getString(R.string.appTitle));
        mTitle.setTypeface(type, Typeface.BOLD);
        setSupportActionBar(mToolbar);
    }

    private void init(){
        img_data_entry=(ImageView)findViewById(R.id.img_data_entry);
        img_data_entry.setOnClickListener(this);
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
            case R.id.img_data_entry:
                Intent intent=new Intent(thisActivity,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
