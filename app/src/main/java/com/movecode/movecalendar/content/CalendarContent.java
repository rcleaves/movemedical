package com.movecode.movecalendar.content;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 */
public class CalendarContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<CalendarItem> ITEMS = new ArrayList<CalendarItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<Integer, CalendarItem> ITEM_MAP = new HashMap<Integer, CalendarItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createCalendarItem(i));
        }
    }

    private static void addItem(CalendarItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(new Integer(item.id), item);
    }

    private static CalendarItem createCalendarItem(int position) {
        return new CalendarItem(String.valueOf(position), "HONOLULU", "details for " + position, new Date());
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * Calendar 'data' class
     */
    @Entity(tableName = "appointments")
    public static class CalendarItem {
        @PrimaryKey(autoGenerate = true)
        public String id;

        @ColumnInfo(name = "date")
        public Date date;

        @ColumnInfo(name = "location")
        public String location;

        @ColumnInfo(name = "details")
        public String details;

        public CalendarItem(String id, String location, String details, Date date) {
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
}