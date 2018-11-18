package com.theman.ruben.dankmeme.custom;

/**
 * Created by Ruben on 19-08-2018.
 */

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetMemeImages extends AsyncTask<Void, String, String>{

    private StringBuffer content;
    private HttpsURLConnection httpsURLConnection = null;

    @Override
    protected String doInBackground(Void... objects) {
        String myUrl = "https://api.imgur.com/3/album/T8HtgII/images";
        try {
            URL url = null;
            url = new URL(myUrl);


            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("Authorization", "Client-ID 6d780d53b5ed06c");
            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            InputStream inputStream = httpsURLConnection.getInputStream();
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(inputStream));
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {

                content.append(inputLine);

            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(content.toString());
        return content.toString();
    }

}



