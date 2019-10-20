package cn.e3mall.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemSearchController {

    @Autowired
    private ItemSearchService itemSearchService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importAllItems(){
        return itemSearchService.importAllItems();
    }
}
