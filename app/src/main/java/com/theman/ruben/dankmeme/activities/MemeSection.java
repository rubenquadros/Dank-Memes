package com.theman.ruben.dankmeme.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.theman.ruben.dankmeme.R;
import com.theman.ruben.dankmeme.fragments.BottomPictureFragment;
import com.theman.ruben.dankmeme.fragments.TopSectionFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MemeSection extends AppCompatActivity implements TopSectionFragment.TopSectionListener{

    private Uri fileUri;
    private Bitmap bitmap;
    private BottomPictureFragment bottomFragment;
    private String myTopText;
    private String myMiddleText;
    private String myBottomText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_section);

        Bundle imageData = getIntent().getExtras();

        if(imageData==null){
            return;
        }

        //Get the image uri from the previous activity
            Intent intent = getIntent();
            String image_path = intent.getStringExtra("imgPath");
            Log.d("9999", image_path);
            fileUri = Uri.parse(image_path);

        //Display the image
            SimpleDraweeView memePicture = (SimpleDraweeView) findViewById(R.id.memePicture);
            memePicture.setImageURI(fileUri);
            memePicture.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void createMeme(String topText, String middleText, String bottomText) throws IOException {
        bottomFragment = (BottomPictureFragment)
                getSupportFragmentManager().findFragmentById(R.id.BottomPictureFragment);

        myTopText = topText;
        myMiddleText = middleText;
        myBottomText = bottomText;

        URL url = new URL(fileUri.toString());

        new AsyncTask<Void, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(Void... voids) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap b) {
                super.onPostExecute(b);
                if(b!=null){
                    updateBitmap(b);
                    Log.d("@@@", "GOT BITMAP");
                }else{
                    Log.d("@@@", "COULD NOT GET BITMAP");
                }
            }
        }.execute();

    }

    private void updateBitmap(Bitmap b) {
        Bitmap myBitmap = null;
        try {
            myBitmap = bottomFragment.setText(this,myTopText, myMiddleText, myBottomText, b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, ShowFinalMeme.class);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);

        intent.putExtra("imgBitmap", byteArrayOutputStream.toByteArray());
        startActivity(intent);
    }
}
