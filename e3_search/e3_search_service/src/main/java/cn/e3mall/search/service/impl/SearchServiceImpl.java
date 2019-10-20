package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.ItemSearchDao;
import cn.e3mall.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ItemSearchDao itemSearchDao;

    @Override
    public SearchResult search(String keyword, int page, int rows) {
        //设置查询条件
        SolrQuery query = new SolrQuery();
        query.setQuery(keyword);
        query.set("df","item_keywords");
        query.setHighlight(true);
        query.setHighlightSimplePre("<em style=\"color:'red'\">");
        query.setHighlightSimplePost("</em>");
        //设置分页
        query.setStart((page-1)*rows);
        query.setRows(rows);
        //从索引库中查询
        SearchResult searchResult = itemSearchDao.getItemListByQuery(query);
        Long recourdCount = searchResult.getRecourdCount();
        int totalPage = (int) (recourdCount/rows);
        if(recourdCount%rows >0){
            totalPage++;
        }
        searchResult.setTotalPages(totalPage);
        return searchResult;
    }
}
