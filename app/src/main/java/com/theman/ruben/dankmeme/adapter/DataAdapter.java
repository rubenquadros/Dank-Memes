package com.theman.ruben.dankmeme.adapter;

/**
 * Created by Ruben on 19-10-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.theman.ruben.dankmeme.R;
import com.theman.ruben.dankmeme.activities.MemeSection;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.Viewholder>{

    private ArrayList<String> meme_data;
    private Context context;

    public DataAdapter(ArrayList<String> meme_data, Context context) {
        this.meme_data = meme_data;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {

        //Loading and displaying the image
        String url = meme_data.get(position);
        Uri uri = Uri.parse(url);
        holder.my_image_view.setImageURI(uri);
        //Set on click
        holder.my_image_view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendImage(view, position);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return meme_data.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        private SimpleDraweeView my_image_view;

        Viewholder(View itemView) {
            super(itemView);
            my_image_view = (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);
        }
    }

    //Method to send the clicked image to another activity
    private void sendImage(View v, int position){
        Intent i = new Intent(context, MemeSection.class);
        String url = meme_data.get(position);
        Uri uri = Uri.parse(url);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("imgPath", uri.toString());
        context.startActivity(i);
    }
}
