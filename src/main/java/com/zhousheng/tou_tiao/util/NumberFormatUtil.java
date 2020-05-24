package com.zhousheng.tou_tiao.util;

public class NumberFormatUtil{
    public static String formatDoubel(double rowDouble, int bit) {
        java.text.NumberFormat ddf1 = java.text.NumberFormat.getNumberInstance();
        ddf1.setMaximumFractionDigits(bit);
        String s = ddf1.format(rowDouble);
        return s;
    }

    public static String formatDoubel(String rowDouble, int bit) {
        java.text.NumberFormat ddf1 = java.text.NumberFormat.getNumberInstance();
        ddf1.setMaximumFractionDigits(bit);
        String s = ddf1.format(rowDouble);
        return s;
    }
}
