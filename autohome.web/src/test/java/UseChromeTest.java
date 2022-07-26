import static org.assertj.core.api.Assertions.assertThat;

import cn.jeneral.common.untils.dynamicwait.DWChromeDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = {"classpath:/spring-cfg/application-servlet.xml"})
public class UseChromeTest {

    @Test
    void test() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        driver.quit();
    }

    @Test
    void test2() {
        DWChromeDriver driver = new DWChromeDriver();
        driver.get("https://www.baidu.com");
        driver.quit();
    }

//    WebDriver driver;

//    @BeforeAll
//    static void setupClass() {
//        WebDriverManager.chromedriver().setup();
//    }
//
//    @BeforeEach
//    void setupTest() {
//        driver = new ChromeDriver();
//    }
//
//    @AfterEach
//    void teardown() {
//        driver.quit();
//    }
//
//    @Test
//    void test() {
//        // Exercise
//        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
//        String title = driver.getTitle();
//
//        // Verify
//        assertThat(title).contains("Selenium WebDriver");
//    }
}
