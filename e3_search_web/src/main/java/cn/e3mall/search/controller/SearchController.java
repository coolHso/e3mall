package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Value("${SEARCH_PAGE_ROW}")
    private Integer row;

    @RequestMapping("/search")
    public String searchBykeyword(String keyword, @RequestParam(defaultValue = "1")Integer page, Model model){
        try {
            keyword = new String(keyword.getBytes("iso-8859-1"),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SearchResult result = searchService.search(keyword, page, row);
        model.addAttribute("query",keyword);
        model.addAttribute("page",page);
        model.addAttribute("recourdCount",result.getRecourdCount());
        model.addAttribute("itemList",result.getItemList());
        model.addAttribute("totalPages",result.getTotalPages());
        return "search";
    }

}
