package com.zhousheng.tou_tiao.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
public class ConfigProvider {
    @Value("${category}")
    public String categoryKey;
    @Value("${mostSellCarIndex}")
    public int mostSellCarIndex;
    @Value("${carInfoUrl}")
    private String carInfoUrl;
}
