package com.readingrecollections.d424_software_engineering_capstone.ui.database;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Converts LocalDate to or from a String
public class DateConverter {

    // Sets format for the converter to YEAR-MONTH-DAY, eg "2025-01-20"
    public static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

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
