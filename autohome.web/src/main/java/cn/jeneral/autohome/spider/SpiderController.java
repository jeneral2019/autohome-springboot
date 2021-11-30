package cn.jeneral.autohome.spider;

import cn.jeneral.biz.autohome.AutohomeSeries;
import cn.jeneral.entity.CarCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("spider")
public class SpiderController{

    @Autowired
    private AutohomeSeries autohomeSeries;

    @Value("jdbc.username")
    private String username;

    @RequestMapping("demo1")
    @ResponseBody
    public String demo1(){
         return "demo1 success!";
    }

    @RequestMapping("demo2")
    @ResponseBody
    public String demo2(String str){
        return "demo2 success!" + "String is " + str;
    }

    @RequestMapping("demo3")
    @ResponseBody
    public String demo3(@RequestBody CarCategory carCategory){
        return "demo3 success!" + "String is " + carCategory.toString();
    }

    @RequestMapping("demo4")
    @ResponseBody
    public String demo4(Long carCategoryId){
        return "demo4 success!" + "carCategory is " + autohomeSeries.getCarCategory(carCategoryId);
    }

    @RequestMapping("demo5")
    @ResponseBody
    public String demo5(){
        return "demo5 success!" + "username is " + username;
    }

}
