package com.movecode.movecalendar.content;

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
     * An array of calendar items.
     */
    public static final List<CalendarItem> ITEMS = new ArrayList<CalendarItem>();

    /**
     * A map of items by ID
     */
    public static final Map<Integer, CalendarItem> ITEM_MAP = new HashMap<Integer, CalendarItem>();

    /*static {
        // create test data
        CalendarDao calendarDao = ItemListFragment.appointmentDatabase.calendarDao();
        if (calendarDao.getAppointments().getValue().size() > 0) {
            ItemListFragment.appointmentDatabase.clearAllTables();
            calendarDao.addAppointment(new CalendarItem(1, "St. George", "appt 1 details", new Date()));
        }
    }*/

    public static void removeAll() {
        ITEMS.clear();
    }

    public static void addItem(CalendarItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(new Integer(item.id), item);
    }
}