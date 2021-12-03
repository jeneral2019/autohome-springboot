package cn.jeneral.common.untils.dynamicwait;


import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author cn.jeneral
 */
public class DWWebDriver {

    protected static Long waitTimeDefault = 20L;
    protected Long waitTime;

    protected WebDriver driver;

    protected DWWebDriver(){}

    protected DWWebDriver(Long waitTime){}

    public DWWebDriver(WebDriver driver){
        this.driver = driver;
        this.waitTime = waitTimeDefault;
    }

    public DWWebDriver(WebDriver driver, Long waitTime){
        this.driver = driver;
        this.waitTime = waitTime;
    }

    public DWWebDriver(WebElement webElement){
        this.driver = ((RemoteWebElement) webElement).getWrappedDriver();
        this.waitTime = waitTimeDefault;
    }

    public DWWebDriver(WebElement webElement, Long waitTime){
        this.driver = ((RemoteWebElement) webElement).getWrappedDriver();
        this.waitTime = waitTime;
    }
    public DWWebDriver setWaitTime(Long time){
        this.waitTime = time;
        return this;
    }

    public void get(String url){
        this.driver.get(url);
    }

    public void quit(){
        this.driver.quit();
    }

    public void switchTo(By by){
        if (!waitEle(by)){
            throw new RuntimeException("cannot find element" + by.toString());
        }
        /**
         * TODO : sometime will throw "Error communicating with the remote browser.It may have died."
         * maybe cause by iframe load slow
         **/
        WebElement webElement = this.driver.findElement(by);
        this.driver = this.driver.switchTo().frame(webElement);
    }

    public void switchToDefault(){
        this.driver = driver.switchTo().defaultContent();
    }
    /**
     * excute js code
     * @param jsCode
     */
    public void executeScript(String jsCode){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        try {
            jse.executeScript(jsCode);
        } catch (Exception e) {
            throw new RuntimeException(""+e.toString());
        }
    }


    /**
     * execute js node with ele
     * @param jsCode
     * @param element
     */
    public void executeScript(String jsCode,WebElement element){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        try {
            jse.executeScript(jsCode,element);
        } catch (Exception e) {
            throw new RuntimeException(""+e.toString());
        }
    }

    /**
     * ===============================================================
     * *******************      url opt     *********************
     * ===============================================================
     **/
    public String getUrl(){
        return driver.getCurrentUrl();
    }

    public Boolean urlContain(String url){
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.urlContains(url));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean urlMatches(String url){
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.urlMatches(url));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * change specified page, index begin with 0
     * @param index
     * @return
     */
    public Boolean changeWindow(int index){
        try{
            WebDriverWait wait = new WebDriverWait(driver, waitTime);
            wait.until(ExpectedConditions.numberOfWindowsToBe(index+1));
        }catch (Exception e){
            return false;
        }
        Set<String> handles =  driver.getWindowHandles();

        List<String> handlesList = new ArrayList(handles);
        driver.switchTo().window(handlesList.get(index));

        return true;
    }

    /**
     * change to next page
     * @return
     */
    public Boolean changeNextWindow(){
        //切换到当前页面的下一个页面
        String handleNow = driver.getWindowHandle();

        try{
            WebDriverWait wait = new WebDriverWait(driver, waitTime);
            wait.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    try {
                        Boolean find = false;
                        for (String handle : driver.getWindowHandles()){
                            if (find){
                                driver.switchTo().window(handle);
                                return true;
                            }
                            if (handleNow.equals(handle)){
                                find = true;
                            }
                        }
                        return false;
                    } catch (WebDriverException var3) {
                        return null;
                    }
                }
                @Override
                public String toString() {
                    return "can not find next page";
                }}
            );
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * ===============================================================
     * *******************      wait element     *********************
     * ===============================================================
     **/

    public Boolean waitEle(By by,Long waitTime) {
        this.waitTime = waitTime;
        return waitEle(by);
    }

    public Boolean waitEle(By by) {
        if (by == null){
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean waitEle(By by, int index) {
        if (by == null || index < 0){
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(finEle -> (driver.findElements(by).size()>index));
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    public Boolean waitEle(By by, int index, Long waitTime) {
        this.waitTime = waitTime;
        return waitEle(by,index);
    }

    public Boolean waitVisEle(By by) {
        if (by == null){
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(DWExpectedConditions.visibilityOfElementsLocatedBy(by));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * ===============================================================
     * *******************      find element     *********************
     * ===============================================================
     **/

    public DWWebElement findElement(By by){
        if (!waitEle(by)){
            return null;
        }
        return new DWWebElement(driver.findElement(by));
    }

    public DWWebElement findElement(By by, int index){
        if (!waitEle(by,index)){
            return null;
        }
        return new DWWebElement(driver.findElements(by).get(index));
    }

    public List<DWWebElement> findElements(By by){
        if (!waitEle(by)){
            return new ArrayList();
        }
        List<WebElement> elementList = driver.findElements(by);
        List<DWWebElement> dwWebElementList = new ArrayList<>();
        for (WebElement element:elementList){
            DWWebElement dwWebElement = new DWWebElement(element);
            dwWebElementList.add(dwWebElement);
        }
        return dwWebElementList;
    }

    public DWWebElement findVisElement(By by){
        if (!waitVisEle(by)){
            return null;
        }
        List<WebElement> webElementList = driver.findElements(by);
        for (WebElement element:webElementList){
            if (element.isDisplayed()){
                return new DWWebElement(element);
            }
        }
        return null;
    }

    public List<DWWebElement> findVisElements(By by){
        if (!waitVisEle(by)){
            return new ArrayList<>();
        }
        List<WebElement> webElementList = driver.findElements(by);
        List<DWWebElement> visWebElementList = new ArrayList<>();
        for (WebElement element:webElementList){
            if (element.isDisplayed()){
                visWebElementList.add(new DWWebElement(element));
            }
        }
        return visWebElementList;
    }

    public DWWebElement selectComboBox(){
        String cssName = ".el-select-dropdown.el-popper";
        By by = By.cssSelector(cssName);

        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            //只要一个元素可见就行
            ExpectedCondition ele = new ExpectedCondition<WebElement>() {
                @Override
                public WebElement apply(WebDriver driver) {
                    List<WebElement> elements = driver.findElements(by);
                    for (WebElement element : elements) {
                        if (element.isDisplayed()) {
                            return element;
                        }
                    }
                    return null;
                }

                @Override
                public String toString() {
                    return "visibility one of element located by " + by;
                }
            };

            wait.until(ele);
            return new DWWebElement((WebElement) ele.apply(driver));

        } catch (Exception e) {
            throw new RuntimeException("cannot find comboBox");
        }
    }

}
