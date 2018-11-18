package com.theman.ruben.dankmeme.fragments;

/**
 * Created by Ruben on 09-10-2017.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.theman.ruben.dankmeme.R;
import com.theman.ruben.dankmeme.custom.MyButtonInterpolator;

import java.io.IOException;

public class TopSectionFragment extends Fragment{

    private EditText topText;
    private EditText middleText;
    private EditText bottomText;

    TopSectionListener activityCommander;

    public interface TopSectionListener{
        void createMeme(String topText, String middleText, String bottomText) throws IOException;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activityCommander = (TopSectionListener) activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_section_fragment, container, false);

        //Reference to the text boxes in layout
        topText = (EditText) view.findViewById(R.id.topText);
        middleText = (EditText) view.findViewById(R.id.middleText);
        bottomText = (EditText) view.findViewById(R.id.bottomText);

        //Reference to the button in the layout
        final Button memeButton = (Button) view.findViewById(R.id.memeButton);
        memeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Add animation to button
                        Animation myAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.bounce);
                        //Use bounce interpolator with amplitude 0.2 and frequency 20
                        MyButtonInterpolator interpolator = new MyButtonInterpolator(0.2, 20);
                        myAnim.setInterpolator(interpolator);
                        memeButton.startAnimation(myAnim);

                        //Add meme text
                        addText(view);
                    }
                }
        );
        return view;
    }

    public void addText(View v){
        try {
            activityCommander.createMeme(topText.getText().toString(), middleText.getText().toString(), bottomText.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
