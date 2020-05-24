package com.zhousheng.tou_tiao.services.articalWriteService.engine;

import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarVersionInfo;
import com.zhousheng.tou_tiao.services.ConfigProvider;
import com.zhousheng.tou_tiao.services.fenchDataService.CarInfoSingleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EngineImpl implements Engine {
    @Autowired
    ConfigProvider configProvider;

    @Override
    public String findEngineInfo(CarInfo carInfo) {
        List<CarVersionInfo> producing = carInfo.findProducingCarVersions(carInfo.getCarVersionInfos());
        Map<String, List<CarVersionInfo>> groups = carInfo.getCarversionGroups(producing, configProvider.getCategoryKey());
        CarVersionInfo carVersionInfo = carInfo.getCarVersionInfos().get(configProvider.getMostSellCarIndex());
        if (configProvider.getCategoryKey().equals("发动机")) {

        }

        configProvider.getCategoryKey();

        return null;
    }

    public static void main(String[] args) {
        CarInfoSingleService carInfoSingleService = new CarInfoSingleService();
        CarInfo carInfo = carInfoSingleService.findCarInfo("https://www.58che.com/2564/items.html");
        EngineImpl engine = new EngineImpl();
        engine.findEngineInfo(carInfo);
    }
}
