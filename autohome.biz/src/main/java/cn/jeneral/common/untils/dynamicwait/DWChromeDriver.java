package cn.jeneral.common.untils.dynamicwait;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author cn.jeneral
 */
public class DWChromeDriver extends DWWebDriver{

    public DWChromeDriver(){
        this.waitTime = waitTimeDefault;
        this.driver = getChromeDriver();
    }

    protected DWChromeDriver(Long time){
        this.waitTime = time;
        this.driver = getChromeDriver();
    }

    private ChromeDriver getChromeDriver(){
        WebDriverManager driverManager = WebDriverManager.chromedriver();

        driverManager.useMirror();
        driverManager.setup();

        return (ChromeDriver) driverManager.create();
    }

}
