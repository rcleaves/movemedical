package com.movecode.movecalendar.content;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = CalendarItem.class, exportSchema = false, version = 1)
public abstract class AppointmentDatabase extends RoomDatabase {
    public static final String DB_NAME = "appointment_db";
    public static AppointmentDatabase instance;

    public static synchronized AppointmentDatabase getInstance(Context ctx) {
        if (instance == null) {
            instance = Room.databaseBuilder(ctx.getApplicationContext(), AppointmentDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // TODO: fix this using coroutines
                    .build();
        }
        return instance;
    }

    public abstract CalendarDao calendarDao();
}
