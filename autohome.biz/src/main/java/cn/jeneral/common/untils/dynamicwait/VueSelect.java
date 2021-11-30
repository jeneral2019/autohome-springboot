package cn.jeneral.common.untils.dynamicwait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cn.jeneral
 */
public class VueSelect {

    private final DWWebElement element;
    private final DWWebDriver driver;
    private final boolean isMulti;
    private int maxIndex;
    private final int maxDeep=10;
    private List<DWWebElement> selectList;
    private List<DWWebElement> tabList;

    public VueSelect(DWWebElement element) {
        String className = element.getAttribute("class");

        String vueSelectName = "el-select-dropdown el-popper";
        if (className == null || !className.contains(vueSelectName)){
            throw new RuntimeException();
        }

        this.element = element;
        this.driver = element.getDWDriver();
        findSelectList(0);
        if (selectList == null){
            this.maxIndex = -1;
        }else {
            this.maxIndex = selectList.size();
        }

        isMulti = className.contains("is-multiple");

    }

    private void findSelectList(int deep){
        if (deep>maxDeep){
            this.selectList = new ArrayList<>();
            this.maxIndex = 0;
            System.out.println("找不到选择");
        }
        List<DWWebElement> selectList = this.element.findVisElements(By.tagName("li"));
        if (selectList == null || selectList.size()==0){
            deep++;
            try {
                Thread.sleep(500*deep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleep"+deep);
            findSelectList(deep);
        }
        this.selectList = selectList;
        this.maxIndex = this.selectList.size();
    }

    public void changeTab(int index){
        if(index<0){
            return;
        }
        String className = "el-tabs__nav";
        if (tabList == null ){
            if (element.waitEleVisInEle(By.className(className))){
                System.out.println("tabList初始化");
                tabList = element.findElements(By.cssSelector(".el-tabs__item.is-top"));
            }else {
                throw new RuntimeException("查找tab失败");
            }

        }
        if (tabList.size()<index){
            throw new RuntimeException("数组越界");
        }
        DWWebElement tabSqEle = tabList.get(index);
        if (tabSqEle.click()){
            findSelectList(0);
        }
    }

    private void setMaxIndex(){
        if (selectList == null){
            this.maxIndex = -1;
        }else {
            this.maxIndex = selectList.size();
        }
    }

    public WebElement getWrappedElement() {
        return element.getWebElement();
    }
    public List<DWWebElement> getSelectList() {
        return selectList;
    }
    public List<DWWebElement> getAllSelected() {
        return getSelectList().stream().filter(VueSelect::isSelected).collect(Collectors.toList());
    }

    public boolean isMultiple() {
        return isMulti;
    }

    public void selectAll(){
        if (!isMulti || selectList == null){
            return;
        }
        for (DWWebElement ele: selectList){
            setSelected(ele,true);
        }
    }

    public void deselectAll(){
        if (!isMulti || selectList == null){
            return;
        }
        for (DWWebElement ele: selectList){
            setSelected(ele,false);
        }
    }

    public Boolean selectByIndex(int index,String hopeUrl) {
        selectByIndex(index);
        return driver.urlContain(hopeUrl);
    }

    public void selectByIndex(int index) {
        if (index > maxIndex){
            throw new RuntimeException("selectByIndex error : index=" + index + ",maxIndex=" +maxIndex);
        }
        setSelected(selectList.get(index),true);
    }

    public void selectByIndexes(List<Integer> indexes) {
        if (!isMulti || indexes == null){
            throw new RuntimeException("selectByIndexes error :" +isMulti + indexes.toString());
        }
        for (int index:indexes){
            selectByIndex(index);
        }
    }

    public void selectByValue(String value) {
        if (value == null){
            return;
        }
        DWWebElement webElement = findEle(By.xpath(".//span[text()=\""+value+"\"]"));
        setSelected(webElement,true);
    }

    private DWWebElement findEle(By by){
        return element.findElement(by);
    }


    public void selectByContainsValue(String value) {
        if (value == null){
            return;
        }
        List<DWWebElement> webElementList = findEles(By.xpath(".//span[contains(text(),\""+value+"\")]"));
        if (webElementList == null){
            return;
        }
        for (DWWebElement webElement:webElementList){
            setSelected(webElement,true);
        }

    }

    private List<DWWebElement> findEles(By by){
        return element.findElements(by);
    }

    public void selectByValues(List<String> values) {
        if (!isMulti || values == null){
            return;
        }
        for (String value:values){
            selectByValue(value);
        }
    }

    private void setSelected(DWWebElement ele,boolean select){
        if (select == !isSelected(ele)){
            ele.click();
        }
    }

    private static Boolean isSelected(DWWebElement isSelect){
        String className = isSelect.getAttribute("class");

        String vueSelectName = "selected";
        if (className == null ){
            throw new RuntimeException("className is null");
        }
        if (className.contains(vueSelectName)){
            return true;
        }
        return false;
    }
}
