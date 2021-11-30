import cn.jeneral.biz.autohome.AutohomeSeries;
import cn.jeneral.entity.CarCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.List;

@ContextConfiguration(locations = {"classpath:/spring-cfg/application-servlet.xml"})
public class MybatisTest extends AbstractTestNGSpringContextTests {


    @Autowired
    AutohomeSeries autohomeSeries;

    @Value("${jdbc.url}")
    String url;

    @Test
    public void demoTest(){
        List<CarCategory> carCategoryList = autohomeSeries.getCarCategory(1L);
        System.out.println(url);
    }


}
