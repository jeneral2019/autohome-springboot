package cn.jeneral.common.untils.dynamicwait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

/**
 * @author cn.jeneral
 */
public class DWExpectedConditions implements ExpectedCondition{

    private DWExpectedConditions() {
    }

    public static ExpectedCondition<Boolean> visibilityOfElementsLocatedBy(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = driver.findElements(locator);
                if (elements == null){
                    return false;
                }
                for (WebElement ele:elements){
                    if (ele.isDisplayed()){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                return "visibility of element located by " + locator;
            }
        };
    }

    public static ExpectedCondition<Boolean> visibilityOfElementsLocatedBy(final WebElement element,final By childLocator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> allChildren = element.findElements(childLocator);
                if (allChildren == null){
                    return false;
                }
                for (WebElement ele:allChildren){
                    if (ele.isDisplayed()){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public String toString() {
                return String.format("visibility of elements located by %s -> %s", element, childLocator);
            }
        };
    }

    public static ExpectedCondition<Boolean> isChange(final WebElement oldEle,final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = driver.findElements(locator);
                if (elements == null){
                    return false;
                }
                return !oldEle.equals(elements.get(0));
            }

            @Override
            public String toString() {
                return "isChange located by " + locator;
            }
        };
    }

    public static ExpectedCondition<Boolean> isChange(final WebElement oldEle,final WebElement parentEle,final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                List<WebElement> elements = parentEle.findElements(locator);
                if (elements == null){
                    return false;
                }
                return !oldEle.equals(elements.get(0));
            }

            @Override
            public String toString() {
                return "isChange located by " + locator;
            }
        };
    }

    @Override
    public Object apply(Object object) {
        return null;
    }
}
