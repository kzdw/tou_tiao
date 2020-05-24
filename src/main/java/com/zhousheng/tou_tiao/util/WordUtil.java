package com.zhousheng.tou_tiao.util;

import com.google.common.collect.Lists;
import com.huaban.analysis.jieba.JiebaSegmenter;

import java.util.List;
import java.util.Random;

public class WordUtil {
    public static String replace(String word,String charSet) {
        List<String> lines = ReadWriteFileUtil.readFileContent("F:\\javaStudy\\projectSamples\\tou_tiao\\src\\main\\resources\\dict_synonym.txt",charSet);
        for (String line : lines) {
            String[] stringsArrays = line.split(" ");
            List<String> stringList = Lists.newArrayList(stringsArrays);
            for (int i = stringList.size() - 1; i >= 0; i--) {
                if (i == 0) {
                    stringList.remove(i);
                }
            }
            for (String s : stringList) {
                if (s.equals(word)) {
                    Random df = new Random();
                    int number = df.nextInt(stringList.size());
                    return stringList.get(number);
                }
            }
        }
        return word;
    }

    public static List<String> cutWords(String rawStr) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        return segmenter.sentenceProcess(rawStr);
    }
}
