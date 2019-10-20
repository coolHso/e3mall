package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult pageQueryByCategoryId(long categoryId, int page,int rows){
        return contentService.getContentsByCategoryId(categoryId,page,rows);
    }


    @RequestMapping("/content/save")
    @ResponseBody
    public E3Result saveContent(TbContent tbContent){
        return contentService.saveContent(tbContent);
    }

    @RequestMapping("/content/edit")
    @ResponseBody
    public E3Result updateContent(TbContent tbContent){
        return contentService.updateContentById(tbContent);
    }

    @RequestMapping("/content/delete")
    @ResponseBody
    public E3Result deleteContent(long[] ids){
        return contentService.deleteContentsById(ids);
    }

}
