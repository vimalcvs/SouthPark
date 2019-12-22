package com.example.southpark.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Series implements Parcelable {

    public static final String IMAGE = "image";
    public static final String SUMMARY = "summary";

    private String image;
    private String summary;

    public Series() {}

    protected Series(Parcel in) {
        image = in.readString();
        summary = in.readString();
    }

    public static final Creator<Series> CREATOR = new Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(summary);
    }

    @Override
    public String toString() {
        return "Series{" +
                "image='" + image + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
