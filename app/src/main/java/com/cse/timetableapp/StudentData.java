package com.cse.timetableapp;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentData implements Parcelable {
    String day,faculty,room,sec,sub,time;
    long per;

    public StudentData() {
    }

    public StudentData(String day, String faculty, long per, String room, String sec, String sub, String time) {
        this.day = day;
        this.faculty = faculty;
        this.per = per;
        this.room = room;
        this.sec = sec;
        this.sub = sub;
        this.time = time;
    }

    protected StudentData(Parcel in) {
        day = in.readString();
        faculty = in.readString();
        per = in.readLong();
        room = in.readString();
        sec = in.readString();
        sub = in.readString();
        time = in.readString();
    }

    public static final Creator<StudentData> CREATOR = new Creator<StudentData>() {
        @Override
        public StudentData createFromParcel(Parcel in) {
            return new StudentData(in);
        }

        @Override
        public StudentData[] newArray(int size) {
            return new StudentData[size];
        }
    };

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public long getPer() {
        return per;
    }

    public void setPer(long per) {
        this.per = per;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(day);
        parcel.writeString(faculty);
        parcel.writeLong(per);
        parcel.writeString(room);
        parcel.writeString(sec);
        parcel.writeString(sub);
        parcel.writeString(time);
    }
}
