package com.cse.timetableapp;

public class SubjectFaculty {
    String id,name,section;

    public SubjectFaculty(String id, String name, String section) {
        this.id = id;
        this.name = name;
        this.section = section;
    }

    public SubjectFaculty() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
