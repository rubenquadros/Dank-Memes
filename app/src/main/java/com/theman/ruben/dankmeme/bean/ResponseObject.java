package com.theman.ruben.dankmeme.bean;

import java.util.ArrayList;

public class ResponseObject {
    private ArrayList<Data> data;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "data=" + data +
                '}';
    }
}
