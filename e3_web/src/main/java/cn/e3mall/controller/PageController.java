package cn.e3mall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 用于跳转主页
 * @author 10642
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String page(){
        return "index";
    }

    @RequestMapping("/{page}")
    public String toPage(String page){
        return page;
    }
}
