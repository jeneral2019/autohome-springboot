package cn.jeneral.common.utils.dynamicwait;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author yuxiangfeng
 * driver 封装
 */
public class FWebDriver implements WebDriver{

    private WebDriver driver;

    private WebDriverManager driverManager;

    private Duration timeout = Duration.ofSeconds(30);

    private Duration pollingEveryTime = Duration.ofSeconds(2);

    public FWebDriver() {
        driverManager = WebDriverManager.getInstance();
        downloadDriver();
        driver = driverManager.create();
    }

    public FWebDriver(DriverManagerType driverManagerType) {
        driverManager = WebDriverManager.getInstance(driverManagerType);
        downloadDriver();
        driver = driverManager.create();
    }

    public void downloadDriver(){
//        driverManager.useMirror();
        driverManager.setup();
    }

    @Override
    public void get(String url){
        if (driver == null){
            throw new RuntimeException("driver 不能为空");
        }
        this.driver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(By by) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                //最大等待时间是60秒
                .withTimeout(timeout)
                //每隔两秒去找一次元素ele是否在页面显示
                .pollingEvery(pollingEveryTime)
                //并且忽略NoSuchElement异常
                .ignoring(NoSuchElementException.class);
        //ele1定位过程使用了对象wait
        List<WebElement> eles = wait.until(new Function<WebDriver, List<WebElement>>() {
            //一个等待的条件
            @Override
            public List<WebElement> apply(WebDriver driver) {
                return driver.findElements(by);
            }
        });

        return  eles;
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    public FWebDriver setTimeOut(Duration duration){
        this.timeout = duration;
        return this;
    }

    public FWebDriver setPollingEveryTime(Duration duration){
        this.pollingEveryTime = duration;
        return this;
    }

    @Override
    public WebElement findElement(By by){

        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                //最大等待时间是60秒
                .withTimeout(timeout)
                //每隔两秒去找一次元素ele是否在页面显示
                .pollingEvery(pollingEveryTime)
                //并且忽略NoSuchElement异常
                .ignoring(NoSuchElementException.class);
        //ele1定位过程使用了对象wait
        WebElement ele = wait.until(new Function<WebDriver, WebElement>() {
            //一个等待的条件
            @Override
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });

        return  ele;
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        if (driver != null){
            this.driver.close();
        }
    }

    @Override
    public void quit() {
        if (driver != null){
            this.driver.quit();
        }
        driver = null;
    }

}