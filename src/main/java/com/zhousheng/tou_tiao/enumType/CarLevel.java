package com.zhousheng.tou_tiao.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CarLevel {
    A0_CAR("A0级轿车"),
    A_CAR("A级轿车"),
    B_CAR("B级轿车"),
    C_CAR("C级轿车"),
    A0_SUV("小型SUV"),
    A_SUV("紧凑型SUV"),
    B_SUV("中型SUV"),
    C_SUV("大型SUV"),
    MPV("MPV"),
    RACING_CAR("跑车");
    private String info;
}
