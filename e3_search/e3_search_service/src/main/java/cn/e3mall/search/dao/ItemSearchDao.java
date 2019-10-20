package cn.e3mall.search.dao;

import cn.e3mall.common.pojo.ItemSearch;
import cn.e3mall.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ItemSearchDao {
    @Autowired
    private SolrServer solrServer;

    public SearchResult getItemListByQuery(SolrQuery query){
        SearchResult searchResult = new SearchResult();
        try {

            QueryResponse response = solrServer.query(query);
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            SolrDocumentList results = response.getResults();
            long numFound = results.getNumFound();
            searchResult.setRecourdCount(numFound);
            ArrayList<ItemSearch> itemList = new ArrayList<>();
            for (SolrDocument result : results) {
                ItemSearch itemSearch = new ItemSearch();
                //设置item标题关键字高亮
                List<String> list = highlighting.get(result.get("id")).get("item_title");
                String title;
                if (list != null && list.size() >0){
                    title = list.get(0);
                }else{
                    title = (String) result.get("item_title");
                }
                itemSearch.setId((String) result.get("id"));
                itemSearch.setCategory_name((String) result.get("item_category_name"));
                itemSearch.setImage((String) result.get("item_image"));
                itemSearch.setPrice((Long) result.get("item_price"));
                itemSearch.setTitle(title);
                itemSearch.setSell_point((String) result.get("item_sell_point"));
                itemList.add(itemSearch);
            }
            searchResult.setItemList(itemList);
            return searchResult;
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
