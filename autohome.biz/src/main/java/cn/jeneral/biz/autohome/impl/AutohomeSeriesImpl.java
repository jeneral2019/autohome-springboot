package cn.jeneral.biz.autohome.impl;

import cn.jeneral.biz.autohome.AutohomeSeries;
import cn.jeneral.common.utils.dynamicwait.DWChromeDriver;
import cn.jeneral.common.utils.dynamicwait.DWWebDriver;
import cn.jeneral.common.utils.dynamicwait.DWWebElement;
import cn.jeneral.common.utils.spider.JsoupUtils;
import cn.jeneral.common.utils.spider.SpiderRule;
import cn.jeneral.dao.autohome.AutoHomeDao;
import cn.jeneral.entity.CarCategory;
import cn.jeneral.entity.FindValue;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AutohomeSeriesImpl implements AutohomeSeries {

    @Autowired
    private AutoHomeDao autoHomeDao;

    @Override
    public List<CarCategory> getCarCategory(Long id){
        return autoHomeDao.selectById(id);
    }

    final String seriesUrl = "https://www.autohome.com.cn/grade/carhtml/";
    final String[] ENG = {"A","B","C","D","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","V","W","X","Y","Z"};

    private DWWebDriver driver = null;

    @Override
    public void autoHomeSpider(){
        getLevel1And2();
        getLevel3();
    }

    @Override
    public void getLevel1And2(){

        List<CarCategory> carCategoryList = new ArrayList<>();
        for (String eng:ENG){
            String url = seriesUrl + eng + ".html";
            SpiderRule rule = new SpiderRule(url, null, null, null, SpiderRule.GET);
            Document doc = JsoupUtils.extract(rule);
            //品牌
            Elements brandEls = doc.select("dl");
            for (Element brandEle : brandEls){
                CarCategory carCategory = new CarCategory();
                String brand = brandEle.select("dt div").select("a").text();
                String brandIdStr = brandEle.attr("id");
                Long brandId = Long.valueOf(brandIdStr);
                carCategory.setBrand(brand);
                carCategory.setSpecId(brandId);
                carCategory.setLevel(1);
                carCategory.setFirstLetter(eng);

                carCategoryList.add(carCategory);

                Elements manEls = brandEle.select(".h3-tit");
                Elements m_manEls = brandEle.select(".rank-list-ul");
                for (int i = 0; i < m_manEls.size(); i++){

                    String manufacturer = manEls.get(i).text();
                    Elements seriesEls = m_manEls.get(i).select("h4");
                    for (int j = 0; j < seriesEls.size(); j++){

                        CarCategory carCategory2 = new CarCategory();
                        carCategory2.setBrand(brand);
                        carCategory2.setParentId(brandId);
                        carCategory2.setLevel(2);
                        carCategory2.setFirstLetter(eng);
                        carCategory2.setCarManufacturer(manufacturer);

                        String seriesUrl = "https:" + seriesEls.get(j).select("a").attr("href");
                        String series = seriesEls.get(j).select("a").text();
                        String seriesIdStr = seriesUrl.substring(28,seriesUrl.indexOf("/#",1));

                        Long seriesId = Long.valueOf(seriesIdStr);

                        if(seriesUrl == null || manufacturer == null || seriesId == null){
                            log.info("==>" + seriesUrl + "  :  " + manufacturer);
                            continue;
                        }

                        carCategory2.setSpecId(seriesId);
                        carCategory2.setSeries(series);
                        carCategory2.setUrl(seriesUrl);

                        carCategoryList.add(carCategory2);
                    }
                }
            }
            autoHomeDao.batchInsert(carCategoryList);
            carCategoryList.clear();
        }
    }

    private List<CarCategory> carCategoryList3 = new ArrayList<>();

    @Override
    public void getLevel3(){

        while (true){
            List<CarCategory> carCategoryList2 = autoHomeDao.selectByLevelAndStatus(2,0);
            if (carCategoryList2.isEmpty()){
                break;
            }

            for (CarCategory carCategory:carCategoryList2){
                carCategoryList3.clear();
                autoHomeDao.modifyStatus(carCategory.getId(),1);
                String url = carCategory.getUrl();
                SpiderRule rule = new SpiderRule(url, null, null, null, SpiderRule.GET);
                Document doc = JsoupUtils.extract(rule);

                carCategory.setCarType(doc.select(".type__item").text());

                if (doc.select(".series-list").size() > 0 ){
                    getLevel3InNormal(carCategory);
                }else if (doc.select(".subnav").size() > 0 ){
                    getLevel3InSpecial(carCategory,doc);
                }else {
                    log.error("cannot find");
                    autoHomeDao.modifyStatus(carCategory.getId(),500);
                }

            }


        }

    }

    @Override
    public void getLevel3InNormalV2(CarCategory parentCarCategory){
        if (driver == null){
            driver = new DWChromeDriver();
        }
        driver.get(parentCarCategory.getUrl());

        //获取list 包括 预售在售和停售款
        List<DWWebElement> listStatusEle = driver.setWaitTime(10L).findElements(By.cssSelector(".list-stats>li"));
        if (listStatusEle.size() == 0){
            autoHomeDao.modifyStatus(parentCarCategory.getId(),20);
            return;
        }

        //遍历所有list
        for(int i=0;i<listStatusEle.size();i++){
            DWWebElement liELe = driver.setWaitTime(10L).findElement(By.cssSelector(".list-stats>li"),i);

            //停售款
            if (liELe.getAttribute("class").equals("more-dropdown")){
                List<DWWebElement> moreListEle = driver.setWaitTime(10L).findElement(By.cssSelector(".list-stats>li"),i).findElements(By.cssSelector("#haltList>li>a"));
                for (int j=0;j<moreListEle.size();j++){
                    DWWebElement moreLiEle = driver.setWaitTime(10L).findElement(By.cssSelector(".list-stats>li"),i).findElement(By.cssSelector("#haltList>li>a"),j);
                    Boolean a = moreLiEle.clickForce(By.cssSelector("#specWrap-3>.halt-spec")) != null;
                    parentCarCategory.setYear(moreLiEle.getAttribute("innerHTML"));
                    getCarCategory("#specWrap-3",parentCarCategory);
                    parentCarCategory.setYear(null);
                }
                continue;
            }
            //预售、在售
            DWWebElement aEle = driver.setWaitTime(10L).findElement(By.cssSelector(".list-stats>li"),i).findElement(By.tagName("a"));
            String id = aEle.getAttribute("data-target");
            Boolean a = aEle.clickForce();
            Boolean b = driver.waitEle(By.cssSelector(id+".active"),5L);
            getCarCategory(id,parentCarCategory);
        }
        autoHomeDao.batchInsert(carCategoryList3);
        autoHomeDao.modifyStatus(parentCarCategory.getId(),2);
        carCategoryList3.clear();

    }


    @Override
    public void getLevel3InNormal(CarCategory parentCarCategory){

        if (driver == null){
            driver = new DWChromeDriver();
        }
        driver.get(parentCarCategory.getUrl());

//        try {
            List<DWWebElement> listStatusEle = driver.setWaitTime(10L).findVisElements(By.cssSelector(".list-stats>li"));
            if (listStatusEle.size() == 0){
                autoHomeDao.modifyStatus(parentCarCategory.getId(),20);
                return;
            }

            for (DWWebElement liELe: listStatusEle){
                //停售款
                if (liELe.getAttribute("class").equals("more-dropdown")){
                    List<DWWebElement> moreListEle = liELe.findElements(By.cssSelector("#haltList>li>a"));
                    for (DWWebElement moreLiEle:moreListEle){
                        Boolean a = moreLiEle.clickForce(By.cssSelector("#specWrap-3>.halt-spec")) != null;
                        parentCarCategory.setYear(moreLiEle.getAttribute("innerHTML"));
                        getCarCategory("#specWrap-3",parentCarCategory);
                        parentCarCategory.setYear(null);
                    }
                    continue;
                }
                //预售、在售
                DWWebElement aEle = liELe.findElement(By.tagName("a"));
                String id = aEle.getAttribute("data-target");
                Boolean a = aEle.clickForce();
                Boolean b = driver.waitEle(By.cssSelector(id+".active"),5L);
                getCarCategory(id,parentCarCategory);
            }
            autoHomeDao.batchInsert(carCategoryList3);
            autoHomeDao.modifyStatus(parentCarCategory.getId(),2);
            carCategoryList3.clear();

//        }catch (Exception e){
//            log.error("getLevel3InNormal e========>"+e.getCause().getMessage());
//            autoHomeDao.modifyStatus(parentCarCategory.getId(),500);
//            return;
//        }
    }

    final Long quickTime = 5L;

    /**
     * 获取车型数据
     * @param id
     * @param parentCarCategory
     */
    private void getCarCategory(String id,CarCategory parentCarCategory){

        //检查该tab下是否存在车型数据
        if (!driver.setWaitTime(quickTime).waitEle(By.cssSelector(id+">dl"))) {
            log.info(id + "is empty");
            return;
        }

        CarCategory carCategoryTemp =  new CarCategory();
        carCategoryTemp.setLevel(3);
        carCategoryTemp.setParentId(parentCarCategory.getSpecId());
        carCategoryTemp.setFirstLetter(parentCarCategory.getFirstLetter());
        carCategoryTemp.setBrand(parentCarCategory.getBrand());
        carCategoryTemp.setSeries(parentCarCategory.getSeries());
        carCategoryTemp.setCarType(parentCarCategory.getCarType());

        DWWebElement specWrap = driver.findElement(By.cssSelector(id));
        List<DWWebElement> dlEleList = driver.findElement(By.cssSelector(id)).findElements(By.tagName("dl"));
        for (int i = 0;i< dlEleList.size();i++){
            String engineStr = null;
            DWWebElement dtEle = driver.findElement(By.cssSelector(id)).findElement(By.tagName("dl"),i).setWaitTime(quickTime).findElement(By.tagName("dt"));
            if (dtEle != null){
                DWWebElement specNameEle = driver.setWaitTime(quickTime).findElement(By.cssSelector(id)).findElement(By.tagName("dl"),i).setWaitTime(quickTime).findElement(By.tagName("dt")).findElement(By.cssSelector(".spec-name"));
                if (specNameEle != null){engineStr = specNameEle.getText();}
            }
            List<DWWebElement> ddEleList = driver.findElement(By.cssSelector(id)).findElement(By.tagName("dl"),i).findElements(By.tagName("dd"));
            for(int j=0;j< ddEleList.size();j++){
                DWWebElement ddEle = driver.findElement(By.cssSelector(id)).findElement(By.tagName("dl"),i).findElement(By.tagName("dd"),j);
                CarCategory carCategory3 = new CarCategory();
                BeanUtils.copyProperties(carCategoryTemp,carCategory3);
                carCategory3.setEngine(engineStr);
                if (id.equals("#specWrap-2")){
                    carCategory3.setYear(ddEle.getAttribute("data-sift1"));
                }else if (id.equals("#specWrap-1")){
                    carCategory3.setYear("-1");
                }
                try {
                    DWWebElement nameEle = ddEle.findElement(By.className("name"));
                    getNameAndSpecId(nameEle,carCategory3);
                }catch (Exception e){
                    log.info("nameEle find error"+e.toString());
                    log.info("set name = 名字未找到 , specId = -");
                    carCategory3.setName("名字未找到");
                }
                try {
                    carCategory3.setPrecursor(ddEle.findElement(By.className("type-default")).getText());
                }catch (Exception e){
                    log.info("set precursor=未知");
                    carCategory3.setPrecursor("未知");
                }

                try {
                    carCategory3.setGear(ddEle.findElement(By.className("type-default"),1).getText());
                }catch (Exception e){
                    log.info("set gear=未知");
                    carCategory3.setGear("未知");
                }
                carCategoryList3.add(carCategory3);
            }
        }
//        for (DWWebElement dlEle:dlEleList){
//
//            DWWebElement dtEle = dlEle.setWaitTime(quickTime).findElement(By.tagName("dt"));
//            DWWebElement engineEle = null;
//            if (dtEle == null){
//                log.info("dtEle is null");
//            }else {
//                engineEle = dtEle.setWaitTime(quickTime).findElement(By.cssSelector(".spec-name"));
//            }
//
//
//            String engineStr = null;
//            if (engineEle != null){
//                engineStr = engineEle.getText();
//            }
//            for (DWWebElement ddEle:dlEle.findElements(By.tagName("dd"))){
//                CarCategory carCategory3 = new CarCategory();
//                BeanUtils.copyProperties(carCategoryTemp,carCategory3);
//                carCategory3.setEngine(engineStr);
//                if (id.equals("#specWrap-2")){
//                    carCategory3.setYear(ddEle.getAttribute("data-sift1"));
//                }else if (id.equals("#specWrap-1")){
//                    carCategory3.setYear("-1");
//                }
//                try {
//                    DWWebElement nameEle = ddEle.findElement(By.className("name"));
//                    getNameAndSpecId(nameEle,carCategory3);
//                }catch (Exception e){
//                    log.info("nameEle find error"+e.toString());
//                    log.info("set name = 名字未找到 , specId = -");
//                    carCategory3.setName("名字未找到");
//                }
//                try {
//                    carCategory3.setPrecursor(ddEle.findElement(By.className("type-default")).getText());
//                }catch (Exception e){
//                    log.info("set precursor=未知");
//                    carCategory3.setPrecursor("未知");
//                }
//
//                try {
//                    carCategory3.setGear(ddEle.findElement(By.className("type-default"),1).getText());
//                }catch (Exception e){
//                    log.info("set gear=未知");
//                    carCategory3.setGear("未知");
//                }
//                carCategoryList3.add(carCategory3);
//            }
//        }
    }

    private void getNameAndSpecId(DWWebElement nameEle,CarCategory carCategory3){
        try{
            carCategory3.setName(nameEle.getText());
        }catch (Exception e){
            log.info("set name = 名字未找到");
            carCategory3.setName("名字未找到");
        }
        try {
            //获取url https://www.autohome.com.cn/spec/45512/#pvareaid=3454492
            String urlStr = nameEle.getAttribute("href");
            URL url = new URL(urlStr);
            //获取fileStr 如 /spec/45512/
            String fileStr = url.getFile();
            //获取specIdStr 45512
            String specIdStr = fileStr.substring(fileStr.indexOf("/",1)+1,fileStr.length()-1);
            carCategory3.setSpecId(Long.valueOf(specIdStr));
        }catch (Exception e){
            log.info("cannot find specId");
        }
    }

    public void getLevel3InSpecial(CarCategory parentCarCategory,Document doc){

        CarCategory carCategory3 = new CarCategory();
        carCategory3.setParentId(parentCarCategory.getSpecId());
        carCategory3.setLevel(3);
        carCategory3.setFirstLetter(parentCarCategory.getFirstLetter());
        carCategory3.setBrand(parentCarCategory.getBrand());
        carCategory3.setSeries(parentCarCategory.getSeries());

        Elements carDetails = doc.select(".car_detail");

        for (Element carDetail:carDetails){
            carCategory3.setYear(carDetail.select(".years").text());
            Elements nameEls = carDetail.select(".name_d .name");
            for (Element nameEle :nameEls){
                carCategory3.setName(nameEle.text());
                String href = nameEle.select("a").attr("href");
                String specIdStr = href.substring(href.indexOf("/",1)+1,href.length()-1);
                carCategory3.setSpecId(Long.valueOf(specIdStr));
                carCategoryList3.add(carCategory3);
            }

        }

        if (!carCategoryList3.isEmpty()){
            autoHomeDao.batchInsert(carCategoryList3);
            autoHomeDao.modifyStatus(parentCarCategory.getSpecId(),2);
        }else {
            autoHomeDao.modifyStatus(parentCarCategory.getSpecId(),20);
        }
        carCategoryList3.clear();
    }
}
