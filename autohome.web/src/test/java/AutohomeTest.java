import cn.jeneral.biz.autohome.AutohomeSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"classpath:/spring-cfg/application-servlet.xml"})
public class AutohomeTest  extends AbstractTestNGSpringContextTests {
    @Autowired
    AutohomeSeries autohomeSeries;

    @Test
    public void getLevel1And2(){
        autohomeSeries.getLevel1And2();
    }

    @Test
    public void getLevel3(){
        autohomeSeries.getLevel3();
    }
}
