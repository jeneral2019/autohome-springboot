package cn.jeneral.entity;

import lombok.Data;

/**
 * @author yuxiangfeng
 */
@Data
public class CarCategory{

    private Long id;
    private Integer level;
    private Long parentId;
    private String firstLetter;
    private String brand;
    private String series;
    private String year;
    private String name;
    private String logo;
    private String carManufacturer;
    private String carType;

    private String url;

    /**
     * 发动机
     */
    private String engine;

    /**
     * 前置四驱
     */
    private String precursor;

    /**
     * 离合档位
     */
    private String gear;

    private Long specId;

    private int status;

}
