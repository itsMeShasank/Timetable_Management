package com.cse.timetableapp;

public class FreeFacultyLoader {
    String facultyid,facultyname;

    public String getFacultyid() {
        return facultyid;
    }

    public void setFacultyid(String facultyid) {
        this.facultyid = facultyid;
    }

    public String getFacultyname() {
        return facultyname;
    }

    public void setFacultyname(String facultyname) {
        this.facultyname = facultyname;
    }

    public FreeFacultyLoader() {
    }

    public FreeFacultyLoader(String facultyid, String facultyname) {
        this.facultyid = facultyid;
        this.facultyname = facultyname;
    }
}
