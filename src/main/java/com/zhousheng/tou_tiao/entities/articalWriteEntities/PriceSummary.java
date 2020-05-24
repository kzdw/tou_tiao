package com.zhousheng.tou_tiao.entities.articalWriteEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceSummary {
    private PriceCompanyAndDealer priceCompanyAndDealer;
    private List<PriceCompanyAndDealer> prices;
}
