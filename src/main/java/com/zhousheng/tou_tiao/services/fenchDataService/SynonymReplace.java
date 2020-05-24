package com.zhousheng.tou_tiao.services.fenchDataService;

import com.zhousheng.tou_tiao.util.WordUtil;

import java.util.List;

public class SynonymReplace {
    public String replace(String rawStr) {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> wordsCut = WordUtil.cutWords(rawStr);
        for (String s : wordsCut) {
            stringBuffer.append(WordUtil.replace(s,"UTF-8"));
        }
        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        SynonymReplace synonymReplace = new SynonymReplace();
        String rawStr = "沃尔沃V60在各方面表现都很均衡，兼容操控性和舒适性的底盘，动力表现和油耗表现也都令人满意。只是内饰设计放今天不算时髦了，车机整体也谈不上好用。";
        String result = synonymReplace.replace(rawStr);
        System.out.println(result);
    }
}
