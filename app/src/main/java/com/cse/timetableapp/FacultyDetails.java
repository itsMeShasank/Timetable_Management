package com.cse.timetableapp;

public class FacultyDetails {
    String Id,Name,Subject;

    public FacultyDetails(String Id, String Name, String Subject) {
        this.Id = Id;
        this.Name = Name;
        this.Subject = Subject;
    }

    public FacultyDetails() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
