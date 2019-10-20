package cn.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class solrTest {
    @Test
    public void addDocument() throws Exception{
    //    创建一个solrserver，使用HttpSolrServer创建对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.131:8080/solr");
        //    创建一个文档对象，solrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //    向文档添加域，必须要id域，其他域名必须在schema.xml中定义
        document.addField("id","test123");
        document.addField("item_title","测试商品");
        document.addField("item_price","888");
    //    将文档添加到索引库
        solrServer.add(document);
    //    提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.131:8080/solr");
        SolrInputDocument document = new SolrInputDocument();
        solrServer.deleteById("test123");
        solrServer.commit();
    }

    //简单查询索引
    @Test
    public void quertDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.131:8080/solr");
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        //执行查询，获得response对象
        QueryResponse response = solrServer.query(query);
        //从response获取查询结果
        SolrDocumentList results = response.getResults();
        //打印结果
        System.out.println(results.getNumFound());
    }
    //复杂查询：高亮显示、默认域、分页
    @Test
    public void heightLightTest() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.131:8080/solr");
        SolrQuery query = new SolrQuery();
        query.setQuery("商品");
       // 指定默认搜索域
        query.set("df","item_keywords");
       // 开启高亮显示
        query.setHighlight(true);
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        //设置分页
        query.setStart(0);
        query.setRows(20);
        // 查询
        QueryResponse response = solrServer.query(query);
        SolrDocumentList results = response.getResults();
        System.out.println(results.getNumFound());
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument result : results) {
            List<String> list = highlighting.get(result.get("id")).get("item_title");
            String itemTitle = null;
            if (null != list && list.size()>0){
                itemTitle = list.get(0);
            }else{
                itemTitle = (String) result.get("item_title");
            }
            System.out.println(itemTitle);
            System.out.println(result.get("item_price"));
        }

    }

}
