package com.example.fastcar.FormatString;

import java.text.DecimalFormat;

public class NumberFormatK {
    public static String format(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        String suffix = "";
        double formattedNumber = number;
        if (number >= 1000) {
            suffix = "K";
            formattedNumber = number / 1000.0;
        }
        DecimalFormat df = new DecimalFormat("#.###");
        String result = df.format(formattedNumber) + suffix;
        return result;
    }
}
