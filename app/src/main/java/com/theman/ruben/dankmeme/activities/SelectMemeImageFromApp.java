package com.theman.ruben.dankmeme.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.theman.ruben.dankmeme.R;
import com.theman.ruben.dankmeme.Utils.Utilities;
import com.theman.ruben.dankmeme.adapter.DataAdapter;
import com.theman.ruben.dankmeme.bean.Data;
import com.theman.ruben.dankmeme.bean.ResponseObject;
import com.theman.ruben.dankmeme.custom.GetMemeImages;

import java.io.IOException;
import java.util.ArrayList;

public class SelectMemeImageFromApp extends AppCompatActivity {

    private ArrayList<Data> data;
    private ArrayList<String> imageUrls = null;
    private RecyclerView recyclerView;

    /*private final String imageUrl[] = {
            "android.resource://com.theman.ruben.dankmeme/drawable/aaaaand_its_gone",
       */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_meme_image_from_app);

        Fresco.initialize(this);
        try {
            initViews();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initViews() throws IOException {

        //Initialize the recycler view
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        //Set the layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new DataAdapter(new ArrayList<>(), this));

        //call to get meme images
        GetMemeImages getMemeImages = new GetMemeImages() {
            ProgressDialog dialog = null;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = Utilities.showProgress(dialog, SelectMemeImageFromApp.this, getString(R.string.loading_memes));
            }

            @Override
            protected void onPostExecute(String response) {
                try {
                    updateAdapter(response);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        getMemeImages.execute();

   }

    private void updateAdapter(String response) {
        Log.d("@@@@", response);
        Gson gson = new Gson();
        ResponseObject responseObject = gson.fromJson(response, ResponseObject.class);
        data = responseObject.getData();
        imageUrls = new ArrayList<>();
        for(int i=0; i<data.size(); i++) {
            Log.d("####", data.get(i).getLink());
            imageUrls.add(data.get(i).getLink());
        }

        DataAdapter adapter = new DataAdapter(imageUrls, this);
        recyclerView.setAdapter(adapter);
    }

}
