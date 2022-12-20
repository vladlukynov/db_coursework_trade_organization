package com.coursework.app.entity;

public class Section {
    private int sectionId;
    private final String sectionName;
    private final Hall hall;
    private final boolean isActive;

    public Section(String sectionName, Hall hall, boolean isActive) {
        this.sectionName = sectionName;
        this.hall = hall;
        this.isActive = isActive;
    }

    public Section(int sectionId, String sectionName, Hall hall, boolean isActive) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.hall = hall;
        this.isActive = isActive;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Hall getHall() {
        return hall;
    }

    public boolean getIsActive() {
        return isActive;
    }
}
