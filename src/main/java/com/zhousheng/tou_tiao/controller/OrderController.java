package com.zhousheng.tou_tiao.controller;

import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.services.ConfigProvider;
import com.zhousheng.tou_tiao.services.articalWriteService.allVersion.AllVersionImpl;
import com.zhousheng.tou_tiao.services.articalWriteService.crashTest.CrashTestImpl;
import com.zhousheng.tou_tiao.services.articalWriteService.engine.EngineImpl;
import com.zhousheng.tou_tiao.services.articalWriteService.introduction.IntroductionImpl;
import com.zhousheng.tou_tiao.services.fenchDataService.CarInfoBatchService;
import com.zhousheng.tou_tiao.services.fenchDataService.CarInfoSingleService;
import com.zhousheng.tou_tiao.services.fenchDataService.CnCapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private IntroductionImpl introductionImpl;
    @Autowired
    private AllVersionImpl allVersion;
    @Autowired
    private CarInfoSingleService carInfoSingleService;
    @Autowired
    private EngineImpl engine;
    @Autowired
    private ConfigProvider configProvider;
    @Autowired
    CrashTestImpl crashTestm;
    @Autowired
    CnCapService cnCapService;
    @Autowired
    CarInfoBatchService carInfoBatchService;

    @RequestMapping("/1")
    public Object getOrder() {
        CarInfo carInfo = carInfoSingleService.findCarInfo(configProvider.getCarInfoUrl());
        String introduction = introductionImpl.findIntroduction(carInfo);
        String allVersionInfo = allVersion.findAllVersionInfo(carInfo);
        engine.findEngineInfo(carInfo);
        String crash = crashTestm.findCrashTestInfo(carInfo, cnCapService.findTestAvarageResult());
     //   carInfoBatchService.findAllCarInfo();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(introduction + allVersionInfo + crash);
        return introduction + allVersionInfo;
    }
}
