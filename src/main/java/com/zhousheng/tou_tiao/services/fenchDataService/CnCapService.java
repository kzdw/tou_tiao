package com.zhousheng.tou_tiao.services.fenchDataService;

import com.zhousheng.tou_tiao.entities.cncap.TestResult;
import com.zhousheng.tou_tiao.entities.cncap.TestYearReslt;
import com.zhousheng.tou_tiao.entities.fenchDataEntities.CnCap;
import com.zhousheng.tou_tiao.util.HttpRequest;
import com.zhousheng.tou_tiao.util.ListUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CnCapService {
    private List<CnCap> findSinglePageResults(int pageNum) {
        String html = HttpRequest.get("http://www.c-ncap.org/cncap/search?manufacturer=&brandId=&carKind=&keyWord=&number=" + pageNum, "UTF-8");
        Document document = Jsoup.parse(html, "UTF-8");
        Elements elements = document.getElementsByTag("tr");
        List<CnCap> cnCaps = new ArrayList<>();
        int endindex = 11;
        if (pageNum == 47) {
            endindex = 4;
        }
        for (int i = 1; i < endindex; i++) {
            Element element = elements.get(i);
            String companyAndSn = element.getAllElements().get(3).text();
            String testYear = element.getAllElements().get(4).text();
            String category = element.getAllElements().get(5).text();
            String score = element.getAllElements().get(6).text().replaceAll("%", "");
            Elements elementsStar = element.getAllElements().get(7).getAllElements().get(2).getAllElements().get(0).getAllElements();
            int index = elementsStar.html().indexOf("stars");
            String star = elementsStar.html().substring(index + 5, index + 6);
            try {
                CnCap cnCap = new CnCap(
                        companyAndSn,
                        testYear,
                        category,
                        score,
                        Double.valueOf(star)
                );
                cnCaps.add(cnCap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("page:" + pageNum);

        for (CnCap cnCap : cnCaps) {
            System.out.println("cncaps:" + cnCap);
        }

        return cnCaps;
    }

    public List<CnCap> finAllPagesResult(int totalPages) {
        List<CnCap> cnCaps = new ArrayList<>();
        for (int i = 1; i < totalPages + 1; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cnCaps.addAll(this.findSinglePageResults(i));
        }
        return cnCaps;
    }

    public Map<String, List<CnCap>> findTestdCarInfo(String carName) {
        List<CnCap> cnCaps = this.finAllPagesResult(47);
        Map<String, List<CnCap>> groups = cnCaps.stream().filter(cnCap -> cnCap.getCompanyAndSn().contains(carName))
                .collect(Collectors.groupingBy(CnCap::getTestYear));
        return groups;
    }

    public List<TestResult> findTestAvarageResult() {
        List<CnCap> cnCaps = this.finAllPagesResult(47);
        Map<String, List<CnCap>> groups = cnCaps.stream()
                .collect(Collectors.groupingBy(CnCap::getCategory));
        List<TestResult> testResults = new ArrayList<>();
        groups.forEach((k, v) -> {
            List<TestYearReslt> testYearReslts = new ArrayList<>();
            TestResult testResult = new TestResult(k, testYearReslts);
            testResults.add(testResult);
            Map<String, List<CnCap>> yestGroup = v.stream()
                    .collect(Collectors.groupingBy(CnCap::getTestYear));
            yestGroup.forEach((k2, v2) -> {
                List<CnCap> cnCapss = v2.stream()
                        .filter(cnCap -> !StringUtils.isEmpty(cnCap.getScore()) && !StringUtils.isEmpty(cnCap.getStar()))
                        .collect(Collectors.toList());
                double avarageSocre = ListUtil.allPlus(cnCapss.stream()
                        .map(cnCap -> cnCap.getScore()).collect(Collectors.toList())) / cnCapss.size();
                double avarageStar = ListUtil.allPlusDoubel(cnCapss.stream()
                        .map(cnCap -> cnCap.getStar()).collect(Collectors.toList())) / cnCapss.size();
                TestYearReslt testYearReslt = new TestYearReslt(
                        k2,
                        avarageSocre,
                        avarageStar
                );
                testYearReslts.add(testYearReslt);
            });

        });

        return testResults;
    }

    public static void main(String[] args) {
        CnCapService cnCapService = new CnCapService();
        Map<String, List<CnCap>> map = cnCapService.findTestdCarInfo("雅阁");
        cnCapService.findTestAvarageResult();

//        List<CnCap> cnCaps = cnCapService.finAllPagesResult(47);
//        long c = cnCaps.stream().filter(cnCap -> cnCap.getTestYear().equals("2019")).count();
//        System.out.println();

    }
}
