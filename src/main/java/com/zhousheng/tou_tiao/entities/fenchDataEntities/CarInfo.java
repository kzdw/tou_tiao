package com.zhousheng.tou_tiao.entities.fenchDataEntities;

import com.zhousheng.tou_tiao.entities.articalWriteEntities.Price;
import com.zhousheng.tou_tiao.entities.articalWriteEntities.PriceCompanyAndDealer;
import com.zhousheng.tou_tiao.entities.articalWriteEntities.PriceSummary;
import com.zhousheng.tou_tiao.enumType.DealerType;
import com.zhousheng.tou_tiao.services.ConfigProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CarInfo {
    ConfigProvider configProvider;
    private String carName;
    private List<CarVersionInfo> carVersionInfos;

    /**
     * 查找在产的车型
     *
     * @param carVersionInfos
     * @return
     */
    public List<CarVersionInfo> findProducingCarVersions(List<CarVersionInfo> carVersionInfos) {
        return carVersionInfos.stream()
                .filter(carVersionInfo -> carVersionInfo.getDetailedInfo().get("生产状态").equals("在产"))
                .collect(Collectors.toList());
    }

    /**
     * 查找指导价和经销商价格区间
     *
     * @param carVersionInfos
     * @param dealerType
     * @return
     */
    public Price findDealerOrCompanyPrice(List<CarVersionInfo> carVersionInfos, DealerType dealerType) {
        try {
            List<Double> prices = new ArrayList<>();
            for (CarVersionInfo carVersionInfo : carVersionInfos) {
                String[] prs = getPriceVirarity(dealerType, carVersionInfo);
                for (String pr : prs) {
                    prices.add(Double.valueOf(pr));
                }
            }

            prices.stream()
                    .sorted()
                    .collect(Collectors.toList());
            return new Price(prices.get(0), prices.get(prices.size() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Price();
    }

    private String[] getPriceVirarity(DealerType dealerType, CarVersionInfo carVersionInfo) {
        String delerPrice = carVersionInfo.getDetailedInfo()
                .get(dealerType.getInfo())
                .replaceAll("万", "")
                .replaceAll("贷", "");
        return delerPrice.split("-");
    }

    public double getTipicalLowestPrice(int index, DealerType dealerType) {
        CarVersionInfo mostSell = this.getCarVersionInfos().get(index);
        String[] prs = this.getPriceVirarity(dealerType, mostSell);
        if (prs.length > 0) {
            return Double.valueOf(prs[0]);
        } else {
            return 0;
        }
    }

    public Double getDiscountRetio(int index) {
        double delerPrice = this.getTipicalLowestPrice(index, DealerType.DEALER);
        double companyPrice = this.getTipicalLowestPrice(index, DealerType.COMPANY);
        if (delerPrice == 0) {
            return 0d;
        } else {
            return (companyPrice - delerPrice) / companyPrice;
        }
    }

    public Double getDiscountPrice(int index) {
        double delerPrice = this.getTipicalLowestPrice(index, DealerType.DEALER);
        double companyPrice = this.getTipicalLowestPrice(index, DealerType.COMPANY);
        if (delerPrice == 0) {
            return 0d;
        } else {
            return companyPrice - delerPrice;
        }
    }

    /**
     * 查找价格总结
     *
     * @param carVersionInfos
     * @param categoryKey     例如 车身结构，排量(L)，变速箱
     * @return
     */
    public PriceSummary findPriceSummary(List<CarVersionInfo> carVersionInfos, String categoryKey) {
        Price priceDealer = this.findDealerOrCompanyPrice(carVersionInfos, DealerType.DEALER);
        Price priceCompany = this.findDealerOrCompanyPrice(carVersionInfos, DealerType.COMPANY);
        PriceCompanyAndDealer priceCompanyAndDealer = new PriceCompanyAndDealer(
                priceDealer,
                priceCompany,
                ""
        );


        Map<String, List<CarVersionInfo>> groups = getCarversionGroups(carVersionInfos, categoryKey);
        List<PriceCompanyAndDealer> list = new ArrayList<>();
        groups.forEach((k, v) -> {
            Price priceD = this.findDealerOrCompanyPrice(v, DealerType.DEALER);
            Price priceC = this.findDealerOrCompanyPrice(v, DealerType.COMPANY);

            PriceCompanyAndDealer priceCandD = new PriceCompanyAndDealer(
                    priceD,
                    priceC,
                    k
            );
            list.add(priceCandD);
        });
        return new PriceSummary(priceCompanyAndDealer, list);
    }

    public CarVersionInfo findTipicalCarInfo() {
        return this.getCarVersionInfos().get(configProvider.getMostSellCarIndex());
    }

    /**
     * 根据指定属性分组
     *
     * @param carVersionInfos
     * @param categoryKey
     * @return
     */
    public Map<String, List<CarVersionInfo>> getCarversionGroups(List<CarVersionInfo> carVersionInfos, String categoryKey) {
        return carVersionInfos
                .stream()
                .collect(Collectors.groupingBy(carInfo1 -> carInfo1.getDetailedInfo().get(categoryKey)));
    }

    public Map<String, CarVersionInfo> getGroupTipicals(List<CarVersionInfo> carVersionInfos, String categoryKey) {
        Map<String, CarVersionInfo> groupTipicals = new LinkedHashMap<>();
        Map<String, List<CarVersionInfo>> groups = this.getCarversionGroups(carVersionInfos, categoryKey);
        groups.forEach((k, v) -> {
            groupTipicals.put(k, v.get(0));
        });
        return groupTipicals;
    }

}
