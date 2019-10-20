package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyTreeNode;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping(value = "/item/cat/list")
    @ResponseBody
    public List<EasyTreeNode> getEasyTreeNodeListByParentId(@RequestParam(name = "id",defaultValue = "0") long parentId){
        return itemCatService.getEasyTreeNodeByParentId(parentId);
    }
}
