package com.example.fastcar.FormatString;

public class NumberFormatVND {
    public static StringBuilder format(int money) {
        StringBuilder sb = new StringBuilder(String.valueOf(money));
        int count = 0;
        for (int i = sb.length() - 1; i >= 0; i--) {
            count++;
            if (count % 3 == 0 && i != 0) {
                sb.insert(i, '.');
            }
        }
        sb.append(" đ");
        return sb;
    }
}
