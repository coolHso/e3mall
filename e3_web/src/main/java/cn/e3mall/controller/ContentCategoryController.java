package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyTreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyTreeNode> contentCategoryList(@RequestParam(name = "id", defaultValue = "0") long parentId){
        return contentCategoryService.getTreeNodeByParentId(parentId);
    }

    @RequestMapping("/content/category/create")
    @ResponseBody
    public E3Result createContentCategory(long parentId, String name){
        return contentCategoryService.createContentCategory(parentId, name);
    }


    @RequestMapping("/content/category/update")
    @ResponseBody
    public E3Result updateContentCategory(long id, String name){
        return contentCategoryService.updateContentCategory(id, name);
    }

    @RequestMapping("/content/category/delete")
    public void deleteContentCategory(long id){
        contentCategoryService.deleteContentCategory(id);
    }

}
