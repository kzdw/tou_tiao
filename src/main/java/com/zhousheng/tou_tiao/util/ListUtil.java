package com.zhousheng.tou_tiao.util;

import java.util.List;
import java.util.Random;

public class ListUtil {
    public static <T> T findRamdomEle(List<T> list) {
        Random random = new Random();
        int n = random.nextInt(list.size());
        return list.get(n);
    }

    public static double allPlus (List<String> strings) {
        double result = 0d;
        for (String string : strings) {
            result+= Double.valueOf(string);
        }
        return result;
    }

    public static double allPlusDoubel (List<Double> doubles) {
        double result = 0d;
        for (Double string : doubles) {
            result+= Double.valueOf(string);
        }
        return result;
    }


}
