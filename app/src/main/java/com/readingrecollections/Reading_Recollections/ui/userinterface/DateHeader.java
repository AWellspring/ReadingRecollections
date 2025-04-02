package com.readingrecollections.Reading_Recollections.ui.userinterface;

public class DateHeader implements Item {
    private String dateRead;

    public DateHeader(String dateRead) {
        this.dateRead = dateRead;
    }

    public String getDateRead() {
        return dateRead;
    }
}
