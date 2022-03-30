package cn.jeneral.common.utils.dynamicwait;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

/**
 * @author jeneral
 */
public class DWChromeDriverV2 extends ChromeDriver {

    static WebDriverManager driverManager;
    static{
        driverManager = WebDriverManager.chromedriver();

        driverManager.useMirror();
        driverManager.setup();
        System.setProperty("webdriver.chrome.driver",driverManager.getDownloadedDriverPath());
    }

    @Override
    protected void setFoundBy(SearchContext context, WebElement element, String by, String using) {
        if (element instanceof DWWebElementV2) {
            DWWebElementV2 dwWebElementV2 = (DWWebElementV2)element;
            dwWebElementV2.setFoundBy(context, by, using);
            dwWebElementV2.setFileDetector(this.getFileDetector());
        }

    }

    @Override
    public List<WebElement> findElements(By by) {
        return by.findElements(this);
    }

    @Override
    public WebElement findElement(By by) {
        return by.findElement(this);
    }

    @Override
    public WebElement findElementById(String using) {
        return this.findElement("id", using);
    }

    @Override
    public List<WebElement> findElementsById(String using) {
        return this.findElements("id", using);
    }
}
