package com.movecode.movecalendar.content;

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
    public static final Map<String, CalendarItem> ITEM_MAP = new HashMap<String, CalendarItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(CalendarItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CalendarItem createPlaceholderItem(int position) {
        return new CalendarItem(String.valueOf(position), "HONOLULU", "details", new Date());
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
    public static class CalendarItem {
        public final String id;
        public final String location;
        public final String details;
        public final Date date;

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