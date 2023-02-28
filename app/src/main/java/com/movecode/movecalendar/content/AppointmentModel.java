package com.movecode.movecalendar.content;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "appointments")
public class AppointmentModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "details")
    private String details;



}
