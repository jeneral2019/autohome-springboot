package cn.jeneral.entity;

import lombok.Data;
import org.openqa.selenium.By;

@Data
public class FindValue {
    private By by;
    private int index;
}
