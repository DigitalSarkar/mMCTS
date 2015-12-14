package com.mcts.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;
import com.mcts.app.R;
import com.mcts.app.utils.TakePictureUtils;
import com.mcts.app.utils.Utils;

import java.io.File;

public class ImageCroppingActivity extends AppCompatActivity {

    private CropImageView cropImageView;
    private ImageView croppedImageView;
    private Button cropButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cropping);

        Intent intent=getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
        cropButton = (Button) findViewById(R.id.Button_crop);

        Uri uri=Uri.parse(imagePath);
        Bitmap image_bitmap = TakePictureUtils.decodeFile(new File(uri.getPath()));
        cropImageView.setImageBitmap(image_bitmap);

        // Initialize the Crop button.
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bitmap croppedImage = cropImageView.getCroppedImage();
//                saveIamge(croppedImage, "CropDemo", "CropDemo");
                String filePath= Utils.saveIamge(croppedImage, "MCTS", "Profile Pic");
//                croppedImageView.setImageBitmap(croppedImage);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("imagePath",filePath);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_cropping, menu);
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
}
