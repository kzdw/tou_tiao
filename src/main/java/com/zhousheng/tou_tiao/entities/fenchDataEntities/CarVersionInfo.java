package com.zhousheng.tou_tiao.entities.fenchDataEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarVersionInfo {
    Map<String, String> detailedInfo = new LinkedHashMap<>();
    private String carVersionName;//车辆版本信息


}
