package com.movecode.movecalendar.content;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalendarDao {

    @Query("select * from appointments")
    List<CalendarItem> getAppointments();

    @Query(("select * from appointments where id = :id"))
    CalendarItem getAppointment(String id);

    @Insert
    void addAppointment(CalendarItem calendarItem);

    @Update
    void updateAppointment(CalendarItem calendarItem);

    @Delete
    void deleteAppointment(CalendarItem calendarItem);

}
