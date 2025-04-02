package com.readingrecollections.Reading_Recollections.ui.userinterface;

public class TitleHeader implements Item {
    private String titleLetter;

    public TitleHeader(char titleLetter) {
        this.titleLetter = String.valueOf(titleLetter);
    }

    public String getTitleLetter() {
        return titleLetter;
    }
}
