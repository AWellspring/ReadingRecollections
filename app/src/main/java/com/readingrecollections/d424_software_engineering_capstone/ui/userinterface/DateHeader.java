package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

public class DateHeader implements Item {
    private String dateRead;

    public DateHeader(String dateRead) {
        this.dateRead = dateRead;
    }

    public String getDateRead() {
        return dateRead;
    }
}
