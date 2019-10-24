package cn.e3mall.search.service.impl;

import cn.e3mall.common.pojo.ItemSearch;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.ItemSearchService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ItemSearchServiceImpl implements ItemSearchService {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SolrServer solrServer;


    @Override
    public E3Result importAllItems() {
        List<ItemSearch> itemList = itemMapper.getItemList();
        //创建solr服务
        try {
            for (ItemSearch itemSearch : itemList) {
                SolrInputDocument document = new SolrInputDocument();
                document.addField("id",itemSearch.getId());
                document.addField("item_title",itemSearch.getTitle());
                document.addField("item_sell_point",itemSearch.getSell_point());
                document.addField("item_price",itemSearch.getPrice());
                document.addField("item_category_name",itemSearch.getCategory_name());
                document.addField("item_image",itemSearch.getImage());
                solrServer.add(document);
            }
            solrServer.commit();
            return E3Result.ok();
        }catch (Exception e){
            e.printStackTrace();
            return E3Result.build(500,"商品导入失败");
        }
    }

    @Override
    public E3Result addDocument(Long itemId) {
        ItemSearch item = itemMapper.getItemById(itemId);
        if(item != null){
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id",item.getId());
            document.addField("item_title",item.getTitle());
            document.addField("item_sell_point",item.getSell_point());
            document.addField("item_image",item.getImage());
            document.addField("item_category_name",item.getCategory_name());
            document.addField("item_price",item.getPrice());
            document.addField("item_desc",item.getDesc());
            try {
                solrServer.add(document);
                solrServer.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return E3Result.ok(item);
        }
        return null;
    }


}
