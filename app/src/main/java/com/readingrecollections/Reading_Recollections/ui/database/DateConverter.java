package com.readingrecollections.Reading_Recollections.ui.database;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Converts LocalDate to or from a String
public class DateConverter {

    // Sets format for the converter to "MMMM d, yyyy", eg "January 20, 2025"
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");

    // Converts from String into LocalDate
    @TypeConverter
    public static LocalDate fromString(String value) {
        return value == null ? null : LocalDate.parse(value, formatter);
    }

    // Converts from LocalDate into String
    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date == null ? null : date.format(formatter);
    }
}
