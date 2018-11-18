package com.theman.ruben.dankmeme.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theman.ruben.dankmeme.R;
import com.theman.ruben.dankmeme.custom.MyButtonInterpolator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

;

public class ShowFinalMeme extends AppCompatActivity {

    private static Bitmap bitmap;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12021995;
    private static final String alertBoxTitle = "Alert!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_final_meme);

        Bundle imageData = getIntent().getExtras();

        if (imageData == null) {
            return;
        }

        //Get the image uri from the previous activity
        Intent intent = getIntent();
        bitmap = BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("imgBitmap"), 0, intent
                        .getByteArrayExtra("imgBitmap").length);

        //Display the image
        ImageView finalImageView = (ImageView) findViewById(R.id.finalImageView);
        finalImageView.setImageBitmap(bitmap);
        finalImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //Reference to the save meme button
        final Button saveButton = (Button) findViewById(R.id.saveButton);

        //Save meme when clicked on it
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Set animation to button
                        Animation myAnim = AnimationUtils.loadAnimation(ShowFinalMeme.this, R.anim.bounce);
                        //Use bounce interpolator with amplitude 0.2 and frequency 20
                        MyButtonInterpolator interpolator = new MyButtonInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);
                        saveButton.startAnimation(myAnim);

                        try {
                            //Save image
                            saveImage(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //Reference to the share meme button
        final Button shareButton = (Button) findViewById(R.id.shareButton);

        //Share meme when clicked on it
        shareButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Set animation to button
                        Animation myAnim = AnimationUtils.loadAnimation(ShowFinalMeme.this, R.anim.bounce);
                        //Use bounce interpolator with amplitude 0.2 and frequency 20
                        MyButtonInterpolator interpolator = new MyButtonInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);
                        shareButton.startAnimation(myAnim);

                        //Share image
                        shareImage(view);
                    }
                }
        );
    }

    public void saveImage(View v) throws IOException {
        //Get directory of pictures
        File imageRoot = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        //Get reference to the edit text
        EditText memeTitle = (EditText) findViewById(R.id.memeTitle);

        //Get the meme title
        String myMemeTitle = memeTitle.getText().toString();

        //Cannot have empty meme title
        if (myMemeTitle.equals("")) {
            createAlertBox(getString(R.string.give_meme_name));
            return;
        }

        //Write meme with title in pictures directory
        File file = new File(imageRoot, myMemeTitle + ".jpg");

        //If meme with same title already exists
        if (file.exists()) {
            createAlertBox(getString(R.string.file_exists));
        } else {
            //Ask permissions for external write
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(this.getApplication(), R.string.storage_request_rationale, Toast.LENGTH_LONG).show();
                    }

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    return;
                }

            }
            //Save image in pictures
            boolean newFile = file.createNewFile();
            if (newFile) {
                Toast.makeText(getApplicationContext(), R.string.meme_saved, Toast.LENGTH_LONG).show();
            }

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, out); // Compress Image
                out.flush();
                out.close();

                //Show image in gallery
                MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shareImage(View v){
        try {
            //Get path of the cache memory
            File cachePath = new File(this.getCacheDir(), "images");

            //Make it the directory
            cachePath.mkdirs();
            
            // overwrites this image every time
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);//compresses image
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Save meme in cache
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");

        //Get uri of the meme stored in cache
        Uri contentUri = FileProvider.getUriForFile(this, "com.theman.ruben.dankmeme.fileprovider", newFile);

        //Share meme via other apps
        if (contentUri != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            Intent.createChooser(intent, "Share via");
            startActivity(intent);
        }
    }

    //Create alert box
    public void createAlertBox(String message){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(alertBoxTitle);
        alertBox.setMessage(message);
        AlertDialog alertDialog = alertBox.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_box);
        alertDialog.show();
    }
}