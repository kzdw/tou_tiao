package com.zhousheng.tou_tiao.entities.fenchDataEntities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CnCap {
    private String companyAndSn;
    private String testYear;
    private String category;
    private String score;
    private double star;

    public CnCap(String testYear) {
        this.testYear = testYear;
    }
}
