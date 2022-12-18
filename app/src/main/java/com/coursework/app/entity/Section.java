package com.coursework.app.entity;

public class Section {
    private final int sectionId;
    private final String sectionName;
    private final Hall hall;
    private final boolean isActive;

    public Section(int sectionId, String sectionName, Hall hall, boolean isActive) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.hall = hall;
        this.isActive = isActive;
    }

    public int getSectionId() {
        return sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public Hall getHall() {
        return hall;
    }

    public boolean isActive() {
        return isActive;
    }
}
