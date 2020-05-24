package com.zhousheng.tou_tiao.services.articalWriteService.crashTest;

import com.zhousheng.tou_tiao.entities.cncap.TestResult;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;

import java.util.List;

public interface CrashTest {
    String findCrashTestInfo(CarInfo carInfo, List<TestResult> cncapTestResult);
}
