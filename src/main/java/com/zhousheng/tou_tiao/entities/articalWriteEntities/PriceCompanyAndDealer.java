package com.zhousheng.tou_tiao.entities.articalWriteEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCompanyAndDealer {
    private Price priceDealer;
    private Price priceCompany;
    private String info;
}
