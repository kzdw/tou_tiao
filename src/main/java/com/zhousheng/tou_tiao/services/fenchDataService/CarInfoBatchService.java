package com.zhousheng.tou_tiao.services.fenchDataService;

import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.repositories.CarInfoQueryRepository;
import com.zhousheng.tou_tiao.util.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarInfoBatchService {
    @Autowired
    private CarInfoQueryRepository repository;

    public List<CarInfo> findOnePageCarinfo(String pageUrl) {
        System.out.println("访问网址：" + pageUrl);
        String html = HttpRequest.get(pageUrl, "UTF-8");
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("s_list clearfix");
        List<String> onePageCars = elements.get(0).getElementsByTag("li").stream()
                .map(element -> element.select("a").attr("href").replaceAll("//", ""))
                .collect(Collectors.toList());
        System.out.println("当前页找到" + onePageCars.size() + "个车辆信息。");
        List<CarInfo> carInfoOnePage = onePageCars.stream().map(singleCar -> {
            return getCarInfo(singleCar);
        }).filter(carInfo1 -> !ObjectUtils.isEmpty(carInfo1)).
                collect(Collectors.toList());
        return carInfoOnePage;
    }

    private CarInfo getCarInfo(String singleCar) {
        try {
            System.out.println("开始访问单个页面：" + singleCar);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String html2 = HttpRequest.get("https://" + singleCar, "UTF-8");
            Document document2 = Jsoup.parse(html2);
            String halfUrl = document2.getElementsByClass("w145 endli").select("a").attr("href");
            String finalUrl = "https://www.58che.com" + halfUrl;
            CarInfoSingleService carInfoSingleService = new CarInfoSingleService();
            CarInfo carInfo = carInfoSingleService.findCarInfo(finalUrl);
            System.out.println("成功获取个单个车辆信息：" + carInfo.getCarName() + "。" + carInfo.getCarVersionInfos().size() + "个版本。");
            repository.save(carInfo);
            return carInfo;
        } catch (Exception e) {
            return null;
        }
    }

    public List<CarInfo> findAllCarInfo() {
        List<String> urlsPages = new ArrayList<>();
        urlsPages.add("https://car.58che.com/series/s5.html");
        for (int i = 2; i < 73; i++) {
            String url = "https://car.58che.com/series/s5_n" + i + ".html";
            urlsPages.add(url);
        }
        List<CarInfo> carInfos = new ArrayList<>();
        for (String urlsPage : urlsPages) {
            carInfos.addAll(this.findOnePageCarinfo(urlsPage));
        }
        return carInfos;
    }

    public static void main(String[] args) {
        CarInfoBatchService carInfoBatchService = new CarInfoBatchService();
        carInfoBatchService.findAllCarInfo();
    }

}

