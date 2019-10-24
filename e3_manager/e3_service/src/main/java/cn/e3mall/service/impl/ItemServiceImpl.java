package cn.e3mall.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Destination topicDestination;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;
    @Value("${ITEM_INFO_TIMEOUT}")
    private Integer ITEM_INFO_TIMEOUT;

    @Override
    public TbItem getItemById(Long itemId) {
        //查询redis中是否有缓存该数据
        try {
            String itemJson = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
            if(StringUtils.isNotBlank(itemJson)){
                return JsonUtils.jsonToPojo(itemJson,TbItem.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        if (null != tbItems && tbItems.size() > 0){
            //将item添加进redis缓存
            try {
                TbItem tbItem = tbItems.get(0);
                jedisClient.set(ITEM_INFO+":"+tbItem.getId()+":BASE",JsonUtils.objectToJson(tbItem));
                jedisClient.expire(ITEM_INFO+":"+tbItem.getId()+":BASE",ITEM_INFO_TIMEOUT);
                return tbItem;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public EasyUIDataGridResult getItemList(int page, int rows){
        //设置分页信息
        PageHelper.startPage(page,rows);
        //通过example查询
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        //创建返回对象
        EasyUIDataGridResult gridResult = new EasyUIDataGridResult();
        gridResult.setRows(tbItems);
        //取分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        long total = pageInfo.getTotal();
        gridResult.setTotal(total);
        return gridResult;
    }

    @Override
    public E3Result addItem(TbItem tbItem, String desc) {
        Date date = new Date();
        //为商品添加id
        final long id = IDUtils.genItemId();
        tbItem.setId(id);
        //为商品插入缺失信息
        tbItem.setStatus((byte) 1);
        tbItem.setUpdated(date);
        tbItem.setCreated(date);
        //将商品添加到数据库
        tbItemMapper.insert(tbItem);
        //为商品描述表添加该商品的描述
        TbItemDesc tbItemDesc = new TbItemDesc(id,date,date,desc);
        tbItemDescMapper.insert(tbItemDesc);
        //发送topic消息，通知更新solr索引库
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(id + "");
            }
        });
        return E3Result.ok();
    }

    @Override
    public E3Result getItemDescById(long id){
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        return new E3Result(tbItemDesc);
    }
    @Override
    public E3Result getItemById(long id){
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        return E3Result.ok(tbItem);
    }

    @Override
    public TbItemDesc getItemDescById(Long id) {
        //查询是否缓存
        try {
            String descJson = jedisClient.get(ITEM_INFO + ":" + id + ":DESC");
            if (StringUtils.isNotBlank(descJson)){
                return JsonUtils.jsonToPojo(descJson,TbItemDesc.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        //将数据添加入缓存
        try {
            jedisClient.set(ITEM_INFO+":"+id+":DESC",JsonUtils.objectToJson(tbItemDesc));
            jedisClient.expire(ITEM_INFO+":"+id+":DESC",ITEM_INFO_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }


}
