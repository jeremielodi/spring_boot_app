package com.digipay.app.models;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Utils {
    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDateTime(Timestamp date) {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return DATE_TIME_FORMAT.format(date);
    }

    public static String generateRamdomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();

    }

    public static String roundToString(Double value, int places) {
        String precision = "";
        for (int i = 0; i < places; i++) {
            precision = precision.concat("0");
        }
        DecimalFormat df = new DecimalFormat("0." + precision);
        // RoundingMode.UP
        df.setRoundingMode(RoundingMode.UP);
        return df.format(value);
    }

}
