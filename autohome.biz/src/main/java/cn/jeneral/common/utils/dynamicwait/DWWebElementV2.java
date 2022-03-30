package cn.jeneral.common.utils.dynamicwait;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.FileDetector;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * @author jeneral
 */
public class DWWebElementV2 extends RemoteWebElement {

    private String foundBy;

    @Override
    protected WebElement findElement(String using, String value) {
        return super.findElement(using,value);
    }

    @Override
    public void sendKeys(CharSequence... keysToSend){
        super.sendKeys(keysToSend);
    }

    @Override
    protected void setFoundBy(SearchContext foundFrom, String locator, String term) {
        this.foundBy = String.format("[%s] -> %s: %s", foundFrom, locator, term);
    }

    @Override
    public void setFileDetector(FileDetector detector) {
        throw new WebDriverException("Setting the file detector only works on remote webdriver instances obtained via RemoteWebDriver");
    }
}
