package com.theman.ruben.dankmeme.fragments;

/**
 * Created by Ruben on 09-10-2017.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theman.ruben.dankmeme.R;

import java.io.IOException;

public class BottomPictureFragment extends Fragment {
    private Bitmap.Config bitmapConfig;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_picture_fragment, container, false);

        return view;

    }

    public Bitmap setText(Context context, String myTopText, String myMiddleText, String myBottomText, Bitmap bitmap) throws IOException {

        Resources resources = context.getResources();

        //Convert pixels to dp
        float scale = resources.getDisplayMetrics().density;

        // set default bitmap config if none
        bitmapConfig = bitmap.getConfig();
        if(bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }

        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        //Set bitmap of image as canvas background
        Canvas canvas = new Canvas(bitmap);

        // new antialiased Paint
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        // Text color
        paint.setColor(Color.WHITE);

        //Make text bold
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Text size in pixels
        paint.setTextSize((int) (20 * scale));

        // Text shadow
        paint.setShadowLayer(5f, 0f, 1f, Color.BLACK);

        // Set text width and height to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);
        int textHeight = canvas.getHeight() - (int) (16 * scale);

        // Init StaticLayout for top meme text
        StaticLayout topTextLayout = new StaticLayout(
                myTopText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Init StaticLayout for middle meme text
        StaticLayout middleTextLayout = new StaticLayout(
                myMiddleText, paint, textWidth / 2, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Init StaticLayout for bottom meme text
        StaticLayout bottomTextLayout = new StaticLayout(
                myBottomText, paint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        //Draw top meme text to the Canvas center at top
        canvas.save();
        canvas.translate(0, 0);
        topTextLayout.draw(canvas);
        canvas.restore();

        //draw middle meme text to the canvas
        canvas.save();
        canvas.translate(0, textHeight - (int) (115 * scale));
        middleTextLayout.draw(canvas);
        canvas.restore();


        //draw bottom meme text to the canvas center at bottom
        canvas.save();
        canvas.translate(0, textHeight - (int) (35 * scale));
        bottomTextLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }
}


