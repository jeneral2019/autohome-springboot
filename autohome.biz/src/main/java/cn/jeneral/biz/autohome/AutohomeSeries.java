package cn.jeneral.biz.autohome;

import cn.jeneral.entity.CarCategory;

import java.util.List;


public interface AutohomeSeries {

    public List<CarCategory> getCarCategory(Long id);

    public void autoHomeSpider();

    public void getLevel1And2();

    public void getLevel3();

    public void getLevel3InNormal(CarCategory parentCarCategory);

    public void getLevel3InNormalV2(CarCategory parentCarCategory);
}
