package cn.jeneral.biz.autohome;

import cn.jeneral.entity.CarCategory;

import java.util.List;


public interface AutohomeSeries {

    public List<CarCategory> getCarCategory(Long id);

    public void autoHomeSpider();

}
