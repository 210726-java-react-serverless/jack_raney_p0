package com.revature.registrar.util;

import java.io.BufferedReader;
import java.util.Calendar;

public class CalendarBuilder {

    private BufferedReader consoleReader;

    public CalendarBuilder(BufferedReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    public Calendar build() throws Exception {
        System.out.println("Date (MM/DD/YYYY): \n" + ">\n");
        String response = consoleReader.readLine();

        String[] vals = response.split("/");
        int month = 0;
        int day = 0;
        int year = 0;
        try {
            month = Integer.parseInt(vals[0]);
            day = Integer.parseInt(vals[1]);
            year = Integer.parseInt(vals[2]);
        } catch (Exception e) {
            return null;
        }

        if(month <= 0 || month > 12) return null;
        if(day <= 0 || day > 31) return null;
        if(year < 2021) return null;

        System.out.println("Time (HH:MM):");

        response = consoleReader.readLine();

        String[] vals2 = response.split(":");
        int hour;
        int minute;
        try {
            hour = Integer.parseInt(vals2[0]);
            minute = Integer.parseInt(vals2[1]);
        } catch (Exception e) {
            return null;
        }

        if(hour <= 0 || hour >= 24) return null;
        if(minute <= 0 || minute >= 60) return null;


        Calendar date = new Calendar.Builder()
                .setCalendarType("iso8601")
                .setDate(year, month - 1, day)
                .setTimeOfDay(hour, minute,0)
                .build();

        return date;
    }
}
