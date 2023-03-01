package com.movecode.movecalendar.content;

import com.movecode.movecalendar.ItemListFragment;

import java.util.Date;
import java.util.ArrayList;
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
        CalendarDao calendarDao = ItemListFragment.appointmentDatabase.calendarDao();

        // create test data
        if (calendarDao.getAppointments().size() > 0) {
            ItemListFragment.appointmentDatabase.clearAllTables();
            calendarDao.addAppointment(new CalendarItem(1, "St. George", "appt 1 details", new Date()));
        }

        // Add from db
        for(CalendarItem calendarItem : calendarDao.getAppointments()) {
            addItem(calendarItem);
        }
    }

    private static void addItem(CalendarItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(new Integer(item.id), item);
    }

    /*private static CalendarItem createCalendarItem(int position) {
        return new CalendarItem(String.valueOf(position), "HONOLULU", "details for " + position, new Date());
    }*/

    /*private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }*/


}