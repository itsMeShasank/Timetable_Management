package com.cse.timetableapp;

import android.os.Parcel;
import android.os.Parcelable;

public class FacultySearchItems implements Parcelable {

    private String name,id;


    public FacultySearchItems() {
    }

    public FacultySearchItems(String name, String id) {
        this.name = name;
        this.id = id;
    }

    protected FacultySearchItems(Parcel in) {
        name = in.readString();
        id = in.readString();
    }

    public static final Creator<FacultySearchItems> CREATOR = new Creator<FacultySearchItems>() {
        @Override
        public FacultySearchItems createFromParcel(Parcel in) {
            return new FacultySearchItems(in);
        }

        @Override
        public FacultySearchItems[] newArray(int size) {
            return new FacultySearchItems[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
    }
}