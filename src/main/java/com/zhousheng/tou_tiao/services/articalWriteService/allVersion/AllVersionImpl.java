package com.zhousheng.tou_tiao.services.articalWriteService.allVersion;

import com.zhousheng.tou_tiao.entities.articalWriteEntities.Price;
import com.zhousheng.tou_tiao.entities.articalWriteEntities.PriceCompanyAndDealer;
import com.zhousheng.tou_tiao.entities.articalWriteEntities.PriceSummary;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarInfo;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CarVersionInfo;
import com.zhousheng.tou_tiao.enumType.DealerType;
import com.zhousheng.tou_tiao.services.ConfigProvider;
import com.zhousheng.tou_tiao.services.LanguageService;
import com.zhousheng.tou_tiao.services.fenchDataService.CarInfoSingleService;
import com.zhousheng.tou_tiao.util.ListUtil;
import com.zhousheng.tou_tiao.util.NumberFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllVersionImpl implements AllVersion {

    @Autowired
    ConfigProvider configProvider;
    @Autowired
    LanguageService languageService;

    @Override
    public String findAllVersionInfo(CarInfo carInfo) {
        List<CarVersionInfo> prducingVersions = carInfo.findProducingCarVersions(carInfo.getCarVersionInfos());
        PriceSummary priceSummary = carInfo.findPriceSummary(prducingVersions, configProvider.getCategoryKey());
        StringBuffer option1 = new StringBuffer();
        option1.append("\r\n");
        if (priceSummary.getPrices().size() > 1) {
            List<PriceCompanyAndDealer> priceCompanyAndDealers = priceSummary.getPrices();
            for (PriceCompanyAndDealer priceCompanyAndDealer : priceCompanyAndDealers) {
                option1.append(priceCompanyAndDealer.getInfo());
                if (priceCompanyAndDealer.getPriceDealer().getHighestPrice() == 0
                        || priceCompanyAndDealer.getPriceDealer().getLowestPrice() == 0) {
                    option1.append("官方指导价：")
                            .append(this.appendPrice(priceCompanyAndDealer.getPriceCompany()));
                } else {
                    option1.append("经销商报价：")
                            .append(this.appendPrice(priceCompanyAndDealer.getPriceDealer()));
                }
            }
        } else {
            option1.append("官方指导价：")
                    .append(this.appendPrice(priceSummary.getPriceCompanyAndDealer().getPriceCompany()
                    ))
                    .append("经销商报价：")
                    .append(this.appendPrice(priceSummary.getPriceCompanyAndDealer().getPriceDealer()
                    ));
        }

        StringBuffer option2 = getStringPriceInfo(carInfo);
        option1.append(option2);
        return option1.toString();
    }

    /**
     * 获取报价优惠信息句子
     *
     * @param carInfo
     * @return
     */
    private StringBuffer getStringPriceInfo(CarInfo carInfo) {
        CarVersionInfo tipical = carInfo.getCarVersionInfos().get(configProvider.getMostSellCarIndex());

        double delerPrice = carInfo.getTipicalLowestPrice(configProvider.getMostSellCarIndex(), DealerType.DEALER);
        double companyPrice = carInfo.getTipicalLowestPrice(configProvider.getMostSellCarIndex(), DealerType.COMPANY);
        StringBuffer option0 = new StringBuffer();
        option0.append(
                ListUtil.findRamdomEle(languageService.getLang1()).replaceAll("#", tipical.getCarVersionName())
        );
        option0
                .append("厂商指导价")
                .append(NumberFormatUtil.formatDoubel(companyPrice, 2))
                .append("万；")
                .append("经销商最低综合优惠价为：")
                .append(NumberFormatUtil.formatDoubel(delerPrice, 2))
                .append("万,");

        double discoutRetion = carInfo.getDiscountRetio(configProvider.getMostSellCarIndex());
        double dicountPrice = carInfo.getDiscountPrice(configProvider.getMostSellCarIndex());
        String endStences = ("大概") +
                NumberFormatUtil.formatDoubel(dicountPrice, 2)
                + ("万。");
        StringBuffer option2 = new StringBuffer();
        if (discoutRetion < 0.05) {
            option2.append("从经销商报价来看，现在综合优惠很少，5%不到，");
        } else if (discoutRetion == 0) {
            option2.append("从经销商报价来看，现在没有优惠。");
        } else if (0.05 <= discoutRetion && discoutRetion < 0.1) {
            option2.append("从经销商报价来看。现在综合优惠不多，7%左右。");
        } else if (0.1 <= discoutRetion && discoutRetion <= 0.15) {
            option2.append("从经销商报价来看。现在综合优惠还不错，12%左右。");
        } else if (0.15 < discoutRetion && discoutRetion <= 0.2) {
            option2.append("从经销商报价来看。现在综合优惠十分可观，17%左右。");
        } else if (0.2 <= discoutRetion && discoutRetion < 0.3) {
            option2.append("从经销商报价来看。现在综合优惠巨大，25%左右。");
        } else if (dicountPrice >= 0.3) {
            option2.append("从经销商报价来看。现在综合优惠十分巨大，已经超过了30%。");
        }
        if (discoutRetion != 0) {
            option2.append(endStences);
        }
        option0.append(option2);
        return option0;
    }

    private String appendPrice(Price price) {
        return this.appendPrice(price.getLowestPrice(), price.getHighestPrice())
                + "万。\r\n";
    }

    private String appendPrice(double lowestPrice, double highestPrice) {
        if (lowestPrice == highestPrice) {
            return String.valueOf(lowestPrice);
        } else {
            return String.valueOf(lowestPrice) +
                    "-" +
                    String.valueOf(highestPrice);
        }


    }

    public static void main(String[] args) {
        AllVersionImpl allVersion = new AllVersionImpl();
        CarInfoSingleService carInfoSingleService = new CarInfoSingleService();
        CarInfo carInfo = carInfoSingleService.findCarInfo("https://www.58che.com/5275/items.html");
        allVersion.findAllVersionInfo(carInfo);
        System.out.println();
    }
}
