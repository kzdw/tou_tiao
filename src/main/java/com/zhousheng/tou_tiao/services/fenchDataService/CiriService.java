package com.zhousheng.tou_tiao.services.fenchDataService;

import com.alibaba.fastjson.JSON;
import com.zhousheng.tou_tiao.util.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CiriService {
    public static void main(String[] args) {
        int pageNum = 1;
        String html = HttpRequest.get("http://www.ciri.ac.cn//queryTestPage.jspx?brandCode=&testProcedure=&testYear=2019%E5%B9%B4&vehicleCode=&vehicleSystemCode=&pageNum=" + pageNum, "UTF-8");
        //Document document = Jsoup.parse(html, "UTF-8");
        Object o =   JSON.parse(html);
      //  document.getElementsByTag("tbody");
    }
}
