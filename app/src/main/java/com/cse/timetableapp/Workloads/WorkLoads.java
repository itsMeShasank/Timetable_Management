package com.cse.timetableapp.Workloads;

import android.os.Parcel;
import android.os.Parcelable;

public class WorkLoads implements Parcelable {
    String faculty_id,workload,faculty_name;

    public WorkLoads(String faculty_id, String workload, String faculty_name) {
        this.faculty_id = faculty_id;
        this.workload = workload;
        this.faculty_name = faculty_name;
    }

    public WorkLoads() {
    }

    protected WorkLoads(Parcel in) {
        faculty_id = in.readString();
        workload = in.readString();
        faculty_name = in.readString();
    }

    public static final Creator<WorkLoads> CREATOR = new Creator<WorkLoads>() {
        @Override
        public WorkLoads createFromParcel(Parcel in) {
            return new WorkLoads(in);
        }

        @Override
        public WorkLoads[] newArray(int size) {
            return new WorkLoads[size];
        }
    };

    public String getFaculty_id() {

        return faculty_id;
    }

    public void setFaculty_id(String faculty_id) {

        this.faculty_id = faculty_id;
    }

    public String getWorkload() {

        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public String getFaculty_name() {
        return faculty_name;
    }

    public void setFaculty_name(String faculty_name) {
        this.faculty_name = faculty_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(faculty_id);
        parcel.writeString(workload);
        parcel.writeString(faculty_name);
    }
}
