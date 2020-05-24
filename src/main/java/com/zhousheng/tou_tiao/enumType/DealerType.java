package com.zhousheng.tou_tiao.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DealerType {
    NONE("NONE"),
    DEALER("经销商报价"),
    COMPANY("厂商指导价");


    private String info;
}
