import cn.jeneral.common.utils.dynamicwait.DWChromeDriverV2;
import cn.jeneral.common.utils.dynamicwait.FWebDriver;
import cn.jeneral.common.utils.dynamicwait.DWWebElementV2;
import cn.jeneral.entity.CarCategory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DemoTest {

    @Test
    public void listAddTest(){
        List<String> stringList = new ArrayList<>();
        String tempStr = "a";

        stringList.add(tempStr);
        System.out.println(stringList.toString());
        //print [a]

        tempStr = "b";
        stringList.add(tempStr);

        System.out.println(stringList.toString());
        //print [a,b]

        List<CarCategory> carCategoryList = new ArrayList<>();
        CarCategory carCategory = new CarCategory();
        for (int i = 1;i<3;i++){
            carCategory.setId(Long.valueOf(i));
            carCategoryList.add(carCategory);
        }
        System.out.println(carCategoryList);
    }

    @Test
    public void test(){

        Scanner myScanner = new Scanner(System.in);
        System.out.println("请输入第一个double");
        double d1 = myScanner.nextDouble();
        System.out.println("请输入第二个double");
        double d2 = myScanner.nextDouble();
        if (d1 > 10.0 && d2 < 20.0){
            System.out.println( d1 + d2 );
        }
        int[] intList = {1,2,3,4,5};
        String a = "";
        
    }

    @Test
    public void DWDriverTest(){
//        System.setProperty("webdriver.chrome.driver","/Users/yuxiangfeng/.cache/selenium/chromedriver/mac64/96.0.4664.45/chromedriver");
        DWChromeDriverV2 driverV2 = new DWChromeDriverV2();
        driverV2.get("https://www.baidu.com");
        WebElement element = (DWWebElementV2)driverV2.findElement(By.id("kw"));
        element.sendKeys("abc");

        driverV2.quit();

    }

    @Test
    public void FDriverTest(){
//        FWebDriver driver = new FWebDriver(DriverManagerType.FIREFOX);
        FWebDriver driver = new FWebDriver();

        driver.get("https://www.baidu.com");

        WebElement ele = driver.findElement(By.id("kw11"));

        driver.quit();
    }

}
