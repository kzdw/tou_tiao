package com.zhousheng.tou_tiao.services.fenchDataService;

import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarVersionInfo;
import com.zhousheng.tou_tiao.enumType.CarLevel;
import com.zhousheng.tou_tiao.services.ConfigProvider;
import com.zhousheng.tou_tiao.util.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CarInfoSingleService {
    public static final String carLevelKey = "carLevel";//对应cncap的称谓 A类乘用车 B类乘用车 MPV SUV 等
    public final static String carLevelCallKey = "carLevelCall";//对车级别的普通叫法 A级轿车 紧凑型SUV 等
    @Autowired
    ConfigProvider configProvider;

    /**
     * 查找车辆头信息
     *
     * @param document
     * @return
     */
    private List<String> findInfoTiles(Document document) {
        List<String> infoTitles = new ArrayList<>();
        Elements elementsOfTitle = document.selectFirst("ul[id~=^allParamTag]").getElementsByTag("li");
        for (int i = 0; i < elementsOfTitle.size(); i++) {
            Element element = elementsOfTitle.get(i);
            infoTitles.add(element.text());
        }
        return infoTitles;

    }

    /**
     * 初始化每个版本的车辆，并把车辆版本信息赋值
     *
     * @param document
     * @return
     */
    private List<CarVersionInfo> getCarVersionNames(Document document) {
        Elements carVersions = document.select("li[id~=^model_name_]");
        List<CarVersionInfo> carVersionInfos = new ArrayList<>();
        for (Element carVersion : carVersions) {
            CarVersionInfo carVersionInfo = new CarVersionInfo();
            String carVersionName = carVersion.getElementsByTag("p").text();
            carVersionInfo.setCarVersionName(carVersionName);
            carVersionInfos.add(carVersionInfo);
            System.out.println(carVersionName);
        }
        return carVersionInfos;
    }

    /**
     * 车辆进行校验
     *
     * @param carVersionInfos
     * @param elements
     */
    private void validate(List<CarVersionInfo> carVersionInfos, Elements elements) {
        if (elements.size() != carVersionInfos.size()) {
            throw new IllegalStateException("车辆个数与参数不对");
        }
    }


    /**
     * 根据连接获取特定车辆信息
     * 例如：https://www.58che.com/3877/items.html
     *
     * @param url
     */
    public CarInfo findCarInfo(String url) {
        String html = HttpRequest.get(url, "GBK");
        Document document = Jsoup.parse(html);
        List<CarVersionInfo> carVersionInfos = this.getCarVersionNames(document);
        List<String> infoTiles = this.findInfoTiles(document);
        this.fillEachVersionCarInfo(document, carVersionInfos, infoTiles);
        String name = findCarName(document);
        CarInfo carInfo = new CarInfo(
                configProvider,
                name,
                carVersionInfos
        );
        this.fillCncapCarLevel(carInfo);
        this.fillGeneralCarLevel(carInfo);
        return carInfo;
    }
    private void fillGeneralCarLevel(CarInfo carInfo) {
        for (CarVersionInfo carVersionInfo : carInfo.getCarVersionInfos()) {
            Map<String, String> detailedInfo = carVersionInfo.getDetailedInfo();
            if (detailedInfo.get("车身结构").equals("MPV")) {
                detailedInfo.put(carLevelCallKey, CarLevel.MPV.getInfo());
            } else if (detailedInfo.get("车身结构").equals("SUV")) {
                if (detailedInfo.get("级别属性").equals("中大型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.C_SUV.getInfo());
                } else if (detailedInfo.get("级别属性").equals("中型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.B_SUV.getInfo());
                } else if (detailedInfo.get("级别属性").equals("紧凑型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.A_SUV.getInfo());
                } else if (detailedInfo.get("级别属性").equals("小型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.A0_SUV.getInfo());
                }
            } else if ((detailedInfo.get("车身结构").equals("三厢车") || detailedInfo.get("车身结构").equals("二厢车"))) {
                if (detailedInfo.get("级别属性").equals("中大型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.C_CAR.getInfo());
                } else if (detailedInfo.get("级别属性").equals("中型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.B_CAR.getInfo());
                } else if (detailedInfo.get("级别属性").equals("紧凑型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.A_CAR.getInfo());
                } else if (detailedInfo.get("级别属性").equals("小型车")) {
                    detailedInfo.put(carLevelCallKey, CarLevel.A0_CAR.getInfo());
                }
            }
        }
    }

    private void fillCncapCarLevel(CarInfo carInfo) {
        for (CarVersionInfo carVersionInfo : carInfo.getCarVersionInfos()) {
            Map<String, String> detailedInfo = carVersionInfo.getDetailedInfo();
            if (detailedInfo.get("车身结构").equals("MPV")) {
                detailedInfo.put(carLevelKey, "MPV");
            } else if (detailedInfo.get("车身结构").equals("SUV")) {
                detailedInfo.put(carLevelKey, "SUV");
            } else if ((detailedInfo.get("车身结构").equals("三厢车") || detailedInfo.get("车身结构").equals("二厢车"))) {
                if (detailedInfo.get("级别属性").equals("中型车")) {
                    detailedInfo.put(carLevelKey, "B类乘用车");
                } else if (detailedInfo.get("级别属性").equals("紧凑型车")) {
                    detailedInfo.put(carLevelKey, "A类乘用车");
                } else if (detailedInfo.get("级别属性").equals("小型车")) {
                    detailedInfo.put(carLevelKey, "小型乘用车");
                }
            }
        }
    }

    /**
     * 获取车名字
     *
     * @param document
     * @return
     */
    private String findCarName(Document document) {
        String str = document.selectFirst("div[class~=^car_banner_l]").getElementsByClass("num").get(0).html();
        int endIndex = str.indexOf("<span");
        String name = str.substring(0, endIndex);
        return name;
    }

    /**
     * 把每个版本的车辆信息写入map
     *
     * @param document
     * @param carVersionInfos
     * @param infoTiles
     */
    private void fillEachVersionCarInfo(Document document, List<CarVersionInfo> carVersionInfos, List<String> infoTiles) {
        Elements elements = document.getElementsByClass("info");
        this.validate(carVersionInfos, elements);
        for (int i = 0; i < elements.size(); i++) {
            CarVersionInfo carVersionInfo = carVersionInfos.get(i);
            Elements elementsLi = elements.get(i).getElementsByTag("li");
            for (int j = 0; j < elementsLi.size(); j++) {
                String value = elements.get(i).getElementsByTag("li").get(j).text();
                if (StringUtils.isEmpty(carVersionInfo.getDetailedInfo().get(infoTiles.get(j)))) {
                    carVersionInfo.getDetailedInfo().put(infoTiles.get(j).replaceAll("\\.","。"), value);
                }
            }
        }
    }

    public static void main(String[] args) {
        CarInfoSingleService carInfoSingleService = new CarInfoSingleService();
        CarInfo carInfo = carInfoSingleService.findCarInfo("https://www.58che.com/2564/items.html");
    }
}
