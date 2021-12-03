import cn.jeneral.entity.CarCategory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class DemoTest {

    @Test
    public void listAddTest(){
        List<String> stringList = new ArrayList<>();
        String tempStr = "a";

        stringList.add(tempStr);
        System.out.println(stringList.toString());
        //print [a]

        tempStr = "b";
        stringList.add(tempStr);

        System.out.println(stringList.toString());
        //print [a,b]

        List<CarCategory> carCategoryList = new ArrayList<>();
        CarCategory carCategory = new CarCategory();
        for (int i = 1;i<3;i++){
            carCategory.setId(Long.valueOf(i));
            carCategoryList.add(carCategory);
        }
        System.out.println(carCategoryList);
    }

}
