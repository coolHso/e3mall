package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/item/{itemId}")
    @ResponseBody
    public TbItem list(@PathVariable Long itemId){
        return itemService.getItemById(itemId);
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(int page, int rows){
        return itemService.getItemList(page,rows);
    }

    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result insertItem(TbItem tbItem, String desc){
        return itemService.addItem(tbItem,desc);
    }

    @RequestMapping("/item/desc/query")
    @ResponseBody
    public E3Result getItemDescById(long id){
        return itemService.getItemDescById(id);
    }

    @RequestMapping("/item/query")
    @ResponseBody
    public E3Result getItemById(long id){
        return itemService.getItemById(id);
    }





}
