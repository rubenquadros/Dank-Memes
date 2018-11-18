package com.theman.ruben.dankmeme.custom;

/**
 * Created by Ruben on 11-11-2017.
 */

public class MyButtonInterpolator implements android.view.animation.Interpolator{

    private double myAmplitude = 1;
    private double myFrequency = 10;

    public MyButtonInterpolator(double myAmplitude, double myFrequency) {
        this.myAmplitude = myAmplitude;
        this.myFrequency = myFrequency;
    }

    @Override
    public float getInterpolation(float time) {
        float interpolationFunc = (float)( -1 * Math.pow(Math.E, -time/myAmplitude) *
                Math.cos(myFrequency * time) + 1);
        return interpolationFunc;
    }
}
