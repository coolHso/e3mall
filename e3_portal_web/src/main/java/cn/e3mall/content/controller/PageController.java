package cn.e3mall.content.controller;

import cn.e3mall.content.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 10642
 */
@Controller
public class PageController {

    @Value("${INDEX_AD1_IMAGE}")
    private Long cid;

    @Autowired
    private ContentService contentService;

    @RequestMapping("/index")
    public String toIndex(Model model){
        model.addAttribute("ad1List",contentService.getTbcontentsByCid(cid));
        return "index";
    }
}
