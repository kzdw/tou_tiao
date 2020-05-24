package com.zhousheng.tou_tiao.services.articalWriteService.introduction;

import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.services.fenchDataService.CarInfoSingleService;
import com.zhousheng.tou_tiao.util.ListUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IntroductionImpl implements Introduction {
    @Override
    public String findIntroduction(CarInfo carInfo) {
        List<String> stringList = new ArrayList<>();
        Map<String, String> singleCar = carInfo.getCarVersionInfos().get(0).getDetailedInfo();
        List<String> options = getFirstSentence(carInfo, singleCar);
        return ListUtil.findRamdomEle(options);
    }

    private List<String> getFirstSentence(CarInfo carInfo, Map<String, String> singleCar) {
        List<String> options = new ArrayList<>();
        String option1 = carInfo.getCarName() +
                "是一款" +
                singleCar.get("所属厂商") +
                "生产的" +
                singleCar.get("级别属性") +
                "。";
        String option2 = singleCar.get("所属厂商") +
                "生产的" +
                carInfo.getCarName() +
                "是一款" +
                singleCar.get("产地属性") +
                "的" +
                singleCar.get("级别属性") +
                "。";

        String option3 = "今天我们来介绍一下：" +
                carInfo.getCarName() +
                ",它是由" +
                singleCar.get("所属厂商") +
                "生产的" +
                singleCar.get("级别属性") +
                "。";
        String option4 = singleCar.get("产地属性") +
                "的" +
                carInfo.getCarName() +
                ",到底怎么样？我们今天说道说道。";
        String option5 = singleCar.get("级别属性") +
                carInfo.getCarName() +
                ",到底怎么样？我们今天来聊聊。";
        String option6 = singleCar.get("所属厂商") +
                "的" +
                singleCar.get("产地属性") +
                "的" +
                singleCar.get("级别属性") +
                carInfo.getCarName() +
                "是一款优秀的车么？";
        String option7 =
                singleCar.get("所属厂商") +
                        "的" +
                        carInfo.getCarName() +
                        "," +
                        "值得买吗？";

        String option8 = "看车无数，买车犯难," +
                option6;
        String option9 = "买车纠结，买车犯难," +
                option7;
        String option10 = "看数据买车," +
                option8;
        String option11 = "车市选车,越选选择越多，" +
                option6;
        String option12 = "买车看数据，" +
                option7;
        String option13 = "用数据看车，看得明白，看得仔细，" +
                option8;
        options.add(option1);
        options.add(option2);
        options.add(option3);
        options.add(option4);
        options.add(option5);
        options.add(option6);
        options.add(option7);
        options.add(option8);
        options.add(option9);
        options.add(option10);
        options.add(option11);
        options.add(option12);
        options.add(option13);
        return options;
    }

    public static void main(String[] args) {
        IntroductionImpl introductionData = new IntroductionImpl();
        CarInfoSingleService carInfoSingleService = new CarInfoSingleService();
        CarInfo carInfo = carInfoSingleService.findCarInfo("https://www.58che.com/5450/items.html");
        introductionData.findIntroduction(carInfo);
    }
}
