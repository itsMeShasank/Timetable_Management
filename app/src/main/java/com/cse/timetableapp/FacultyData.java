package com.cse.timetableapp;


import android.os.Parcel;
import android.os.Parcelable;

public class FacultyData implements Parcelable {

    String facultyId,period,sectionId,shortVal,room;

    public FacultyData(String facultyId, String period, String sectionId, String shortVal, String room) {
        this.facultyId = facultyId;
        this.period = period;
        this.sectionId = sectionId;
        this.shortVal = shortVal;
        this.room = room;
    }

    public FacultyData() {
    }

    protected FacultyData(Parcel in) {
        facultyId = in.readString();
        period = in.readString();
        sectionId = in.readString();
        shortVal = in.readString();
        room = in.readString();
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

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(facultyId);
        dest.writeString(period);
        dest.writeString(sectionId);
        dest.writeString(shortVal);
        dest.writeString(room);
    }
}
