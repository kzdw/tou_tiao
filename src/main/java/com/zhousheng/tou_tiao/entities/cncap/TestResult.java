package com.zhousheng.tou_tiao.entities.cncap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResult {
    private String carType;
    List<TestYearReslt> testYearReslts;
}
