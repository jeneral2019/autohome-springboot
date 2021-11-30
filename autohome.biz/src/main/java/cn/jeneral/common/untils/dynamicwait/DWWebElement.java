package cn.jeneral.common.untils.dynamicwait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn.jeneral
 */
public class DWWebElement {
    private static Long waitTimeDefault = 20L;
    private Long waitTime;
    private WebElement webElement;
    private WebDriver driver;
    private DWWebDriver DWWebDriver;
    private static int maxDeep =20;

    public DWWebElement(WebElement webElement){
        if (webElement == null){throw new RuntimeException("webElement is null");}
        this.webElement = webElement;
        this.driver = ((RemoteWebElement) webElement).getWrappedDriver();
        this.DWWebDriver = new DWWebDriver(this.driver);
        this.waitTime = waitTimeDefault;
    }

    public DWWebElement(WebElement webElement, Long waitTime){
        if (webElement == null || waitTime == null ){throw new RuntimeException("webElement or waitTime is null");}
        this.webElement = webElement;
        this.driver = ((RemoteWebElement) webElement).getWrappedDriver();
        this.DWWebDriver = new DWWebDriver(this.driver);
        this.waitTime = waitTime;
    }

    public WebElement getWebElement(){
        return this.webElement;
    }
    public WebDriver getDriver(){return this.driver;}
    public DWWebDriver getDWDriver(){return this.DWWebDriver;}

    public Boolean isDisplayed(){
        return webElement.isDisplayed();
    }

    public Boolean isChange(By eleBy){
        WebDriverWait wait = new WebDriverWait(driver, 5L);
        try{
            wait.until(DWExpectedConditions.isChange(webElement,eleBy));
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean isChange(WebElement parentEle,By eleBy){
        WebDriverWait wait = new WebDriverWait(driver, 5L);
        try{
            wait.until(DWExpectedConditions.isChange(webElement,parentEle,eleBy));
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public String getAttribute(String name){
        return webElement.getAttribute(name);
    }

    public String getText(){
        return webElement.getText();
    }

    public DWWebElement setWaitTime(Long newWaitTime){
        this.waitTime = newWaitTime;
        return this;
    }
    /**
     * ===============================================================
     * *******************      wait element     *********************
     * ===============================================================
     **/

    public Boolean waitEleVis() {
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.visibilityOf(this.webElement));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean waitEleVis(Long waitTime) {
        this.waitTime = waitTime;
        return waitEleVis();
    }

    /**
     * wait element , by is find in element
     * @param by
     * @return
     */
    public Boolean waitEleInEle(By by) {

        if (by == null){
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(this.webElement,by));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public Boolean waitEleInEle(By by, Long waitTime) {
        this.waitTime = waitTime;
        return waitEleInEle(by);
    }

    public Boolean waitEleInEle(By by,int index) {

        if (by == null){
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(finEle -> (this.webElement.findElements(by).size()>index));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public Boolean waitEleInEle(By by,int index, Long waitTime) {
        this.waitTime = waitTime;
        return waitEleInEle(by,index);
    }

    public Boolean waitEleVisInEle(By by) {

        if (by == null){
            return false;
        }
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(DWExpectedConditions.visibilityOfElementsLocatedBy(this.webElement,by));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public Boolean waitEleVisInEle(By by, Long waitTime) {
        this.waitTime = waitTime;
        return waitEleVisInEle(by);
    }




    /**
     * ===============================================================
     * *******************      find element     *********************
     * ===============================================================
     **/
    public DWWebElement findElement(By by){
        if (!waitEleInEle(by)){
            return null;
        }
        return new DWWebElement(webElement.findElement(by));
    }
    public DWWebElement findElement(By by, int index){
        if (!waitEleInEle(by,index)){
            return null;
        }
        return new DWWebElement(webElement.findElements(by).get(index));
    }
    public List<DWWebElement> findElements(By by) {
        if (!waitEleInEle(by)){
            return new ArrayList();
        }
        List<WebElement> elementList = webElement.findElements(by);
        List<DWWebElement> dwWebElementList = new ArrayList<>();
        for (WebElement element:elementList){
            dwWebElementList.add(new DWWebElement(element));
        }
        return dwWebElementList;
    }

    public List<DWWebElement> findVisElements(By by) {
        if (!waitEleVisInEle(by)){
            return new ArrayList<>();
        }
        List<WebElement> webElementList = webElement.findElements(by);
        List<DWWebElement> webElementVisList = new ArrayList<>();
        for (WebElement webElement:webElementList){
            if (webElement.isDisplayed()){
                webElementVisList.add(new DWWebElement(webElement));
            }
        }
        return webElementVisList;
    }

    /**
     * ===============================================================
     * *******************      click element     *********************
     * ===============================================================
     **/

    private Boolean isClick(){
        WebDriverWait wait = new WebDriverWait(driver, waitTime);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Boolean isClick(Long waitTime){
       this.waitTime = waitTime;
       return isClick();
    }

    public Boolean click(){
        if (!isClick()){
            return false;
        }
        try{
            //元素点击需要元素出现在浏览器可见位置，所以需要滚动页面到该元素
            Actions actions = new Actions(driver);
            actions.moveToElement(webElement).perform();

            //TODO 上述方法仅能纵向移动，无法横向移动

        }catch (Exception e){
            return false;
        }

        try {
            webElement.click();
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean clickForce(){
        try {
            String clickJs = "arguments[0].click();";
            DWWebDriver.executeScript(clickJs,webElement);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public DWWebElement clickForce(By hopeBy){
        try {
            String clickJs = "arguments[0].click();";
            DWWebDriver.executeScript(clickJs,webElement);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
        return DWWebDriver.findElement(hopeBy);
    }

    public Boolean clickHope(String hopeUrl) {
        if (!click()){
            return false;
        }
        return DWWebDriver.urlContain(hopeUrl);
    }

    public DWWebElement clickHope(By hopeBy) {
        if (!click()){
            return null;
        }
        return DWWebDriver.findElement(hopeBy);
    }

    public DWWebElement clickHope(By hopeBy, int hopeIndex) {
        if (!click()){
            return null;
        }
        return DWWebDriver.findElement(hopeBy,hopeIndex);
    }

    public DWWebElement clickHope(WebElement element, By hopeBy) {
        if (!click()){
            return null;
        }
        return new DWWebElement(element).findElement(hopeBy);
    }

    public DWWebElement clickHope(WebElement element, By hopeBy, int hopeIndex) {
        if (!click()){
            return null;
        }
        return new DWWebElement(element).findElement(hopeBy,hopeIndex);
    }


    /**
     * ===============================================================
     * *******************      send keys     *********************
     * ===============================================================
     **/


    String VALUE = "value";
    public Boolean sendKey(String key) {
        if (!waitEleVis()){
            throw new RuntimeException("element is not vis,cannot sendKey");
        }
        if (key == null){
            return false;
        }
        if (!webElement.getAttribute(VALUE).isEmpty()){
            webElement.clear();
        }
        webElement.sendKeys(key);
        return key.equals(webElement.getAttribute(VALUE));
    }

    public DWWebElement sendKey(String key, By hopeBy) {
        return sendKey(key)? DWWebDriver.findElement(hopeBy):null;
    }


    public DWWebElement sendKey(String key, By hopeBy, int hopeIndex) {
        return sendKey(key)? DWWebDriver.findElement(hopeBy,hopeIndex):null;
    }


    public Boolean actionChains(){
        Actions actions = new Actions(driver);
        try {
            actions.moveToElement(webElement).perform();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
