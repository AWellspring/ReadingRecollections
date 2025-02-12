package com.readingrecollections.d424_software_engineering_capstone.ui.userinterface;

public class TitleHeader implements Item {
    private String titleLetter;

    public TitleHeader(char titleLetter) {
        this.titleLetter = String.valueOf(titleLetter);
    }

    public String getTitleLetter() {
        return titleLetter;
    }
}
