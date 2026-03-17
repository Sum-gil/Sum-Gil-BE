package com.example.sum_gil_be.dashboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherBaseTimeUtils {

    private WeatherBaseTimeUtils() {
    }

    public static String getBaseDate(LocalDateTime now) {
        LocalDateTime adjusted = now.minusHours(1);
        return adjusted.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String getBaseTime(LocalDateTime now) {
        LocalDateTime adjusted = now.minusHours(1);
        return String.format("%02d00", adjusted.getHour());
    }
}