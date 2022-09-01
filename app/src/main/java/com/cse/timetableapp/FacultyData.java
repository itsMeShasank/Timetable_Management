package com.cse.timetableapp;


import android.os.Parcel;
import android.os.Parcelable;

public class FacultyData implements Parcelable {

    private String day,name,sectionId,shortVal,room,time;
    private long period;

    public FacultyData() {
    }

    public FacultyData(String day, String name, String sectionId, String shortVal, String room, String time, long period) {
        this.day = day;
        this.name = name;
        this.sectionId = sectionId;
        this.shortVal = shortVal;
        this.room = room;
        this.time = time;
        this.period = period;
    }


    protected FacultyData(Parcel in) {
        day = in.readString();
        name = in.readString();
        sectionId = in.readString();
        shortVal = in.readString();
        room = in.readString();
        time = in.readString();
        period = in.readLong();
    }

    public static final Creator<FacultyData> CREATOR = new Creator<FacultyData>() {
        @Override
        public FacultyData createFromParcel(Parcel in) {
            return new FacultyData(in);
        }

        @Override
        public FacultyData[] newArray(int size) {
            return new FacultyData[size];
        }
    };

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getShortVal() {
        return shortVal;
    }

    public void setShortVal(String shortVal) {
        this.shortVal = shortVal;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(day);
        parcel.writeString(name);
        parcel.writeString(sectionId);
        parcel.writeString(shortVal);
        parcel.writeString(room);
        parcel.writeString(time);
        parcel.writeLong(period);
    }
}
