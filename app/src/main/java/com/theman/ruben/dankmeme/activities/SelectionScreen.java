package com.theman.ruben.dankmeme.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import com.theman.ruben.dankmeme.R;
import com.theman.ruben.dankmeme.custom.MyButtonInterpolator;

public class SelectionScreen extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private Snackbar snackbar;
    private View mainView;
    private Button selectImage;
    private Button galleryButton;
    private Button tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);
        mainView = findViewById(R.id.mainLayout);
        selectImage = (Button) findViewById(R.id.appImagesButton);
        galleryButton = (Button) findViewById(R.id.galleryImagesButton);
        tryAgain = (Button) findViewById(R.id.tryAgainButton);
        if (!checkInternet()) {
            updateView();
        }else{
            tryAgain.setVisibility(View.GONE);
            selectImage.setVisibility(View.VISIBLE);
            galleryButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        tryAgain.setVisibility(View.VISIBLE);
        selectImage.setVisibility(View.GONE);
        galleryButton.setVisibility(View.GONE);
        snackbar = Snackbar.make(mainView, R.string.no_internet, Snackbar.LENGTH_INDEFINITE).
                setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
        snackbar.show();
    }


    private Boolean checkInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }

    //When app images is clicked
    public void chooseImage(View view) {
        //Get Reference to button
        Button appImagesButton = (Button) findViewById(R.id.appImagesButton);

        //Set animation to button
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        //Use bounce interpolator with amplitude 0.2 and frequency 20
        MyButtonInterpolator interpolator = new MyButtonInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        appImagesButton.startAnimation(myAnim);

        //Go to select meme images activity
        Intent i = new Intent(this, SelectMemeImageFromApp.class);
        startActivity(i);
    }

    //when choose from gallery is clicked
    public void chooseGallery(View view) {
        //Get reference to button
        Button galleryImagesButton = (Button) findViewById(R.id.galleryImagesButton);

        //Set animation to button
        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        //Use bounce interpolator with amplitude 0.2 and frequency 20
        MyButtonInterpolator interpolator = new MyButtonInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        galleryImagesButton.startAnimation(myAnim);

        //Open gallery
        openImageChooser();
    }

    void openImageChooser(){
        //Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    //when click on try again
    public void tryAgain(View view){
        if(!checkInternet()){
            updateView();
        }else{
            tryAgain.setVisibility(View.GONE);
            selectImage.setVisibility(View.VISIBLE);
            galleryButton.setVisibility(View.VISIBLE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();

                //Send the image uri to the next activity
                Intent i = new Intent(this, MemeSection.class);
                i.putExtra("imgPath",selectedImage.toString());
                startActivity(i);
            } else {
                Toast.makeText(this, R.string.no_image_picked,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.oops_err, Toast.LENGTH_LONG).show();
        }
    }
}
