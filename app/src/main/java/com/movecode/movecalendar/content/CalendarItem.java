package com.movecode.movecalendar.content;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

/**
 * Calendar 'data' class
 */
@Entity(tableName = "appointments")
@TypeConverters({Converters.class})
public class CalendarItem {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @ColumnInfo(name = "date")
    public Date date;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "details")
    public String details;

    public CalendarItem(Integer id, String location, String details, Date date) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.details = details;
    }

    @Override
    public String toString() {
        return details;
    }
}
