package com.zhousheng.tou_tiao.services.articalWriteService.crashTest;

import com.zhousheng.tou_tiao.entities.cncap.TestResult;
import com.zhousheng.tou_tiao.entities.cncap.TestYearReslt;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarVersionInfo;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CnCap;
import com.zhousheng.tou_tiao.services.fenchDataService.CarInfoSingleService;
import com.zhousheng.tou_tiao.services.fenchDataService.CnCapService;
import com.zhousheng.tou_tiao.util.NumberFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

@Service
public class CrashTestImpl implements CrashTest {
    @Autowired
    CnCapService cnCapService;

    @Override
    public String findCrashTestInfo(CarInfo carInfo, List<TestResult> cncapTestResult) {
        String result = "";
        CarVersionInfo carVersionInfo = carInfo.findTipicalCarInfo();
        String carlevel = carVersionInfo.getDetailedInfo().get(CarInfoSingleService.carLevelKey);
        TestResult testResult = cncapTestResult.stream()
                .filter(test -> test.getCarType().equals(carlevel))
                .findFirst()
                .orElse(null);
        Map<String, List<CnCap>> map = cnCapService.findTestdCarInfo(carInfo.getCarName());
        CnCap carCncapTestResult = this.findCarMostRecentCrashResult(map);

        TestYearReslt testYearReslt = testResult.getTestYearReslts().stream()
                .filter(testYearResl -> testYearResl.getTestYear().equals(carCncapTestResult.getTestYear()))
                .findFirst().orElse(null);
        if (ObjectUtils.isEmpty(testYearReslt)) {
            return "";
        } else {
            result = carInfo.getCarName() +
                    "在" +
                    carCncapTestResult.getTestYear() +
                    "年的C-NCAP(中国新车评价规程)的碰撞测试中获得" +
                    carCncapTestResult.getScore() +
                    "分，" +
                    carCncapTestResult.getStar() +
                    "星。" +
                    testYearReslt.getTestYear() +
                    "年度"+
                    testResult.getCarType() +
                    "的平均得分为" +
                    NumberFormatUtil.formatDoubel(  testYearReslt.getAvarageSocre(),2) +
                    "," +
                    testYearReslt.getAvarageStar()+
                    "星。";

        }
        return result;
    }

    private CnCap findCarMostRecentCrashResult(Map<String, List<CnCap>> map) {
        if (!CollectionUtils.isEmpty(map.get("2020"))) {
            return map.get("2020").get(0);
        } else if (!CollectionUtils.isEmpty(map.get("2019"))) {
            return map.get("2019").get(0);
        } else if (!CollectionUtils.isEmpty(map.get("2018"))) {
            return map.get("2018").get(0);
        } else if (!CollectionUtils.isEmpty(map.get("2017"))) {
            return map.get("2017").get(0);
        } else if (!CollectionUtils.isEmpty(map.get("2016"))) {
            return map.get("2016").get(0);
        }
        return new CnCap("2000000");
    }
}
