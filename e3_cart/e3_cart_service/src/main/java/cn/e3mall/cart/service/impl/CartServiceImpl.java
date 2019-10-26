package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_KEY}")
    private String redisCartKey;
    @Value("${REDIS_CART_EXPIRE}")
    private Integer redisCartExpire;

    @Override
    public E3Result addCart(Long userId, Long itemId, Integer num){
        //根据userId查询redis中的购物车信息
        Boolean hexists = jedisClient.hexists(redisCartKey + ":" + userId, itemId + "");
        if (hexists) {
        //    存在
            String itemJson = jedisClient.hget(redisCartKey + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);
        //    写回redis
            jedisClient.hset(redisCartKey + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));
        }else {
            TbItem item = itemMapper.selectByPrimaryKey(itemId);
            item.setNum(num);
            String images = item.getImage();
            if (images != null){
                item.setImage(images.split(",")[0]);
            }
            jedisClient.hset(redisCartKey + ":" + userId, itemId + "",JsonUtils.objectToJson(item));
        }
        return E3Result.ok();
    }

    @Override
    public List<TbItem> getItemsFromCart(Long userId) {
        List<String> itemsJson = jedisClient.hvals(redisCartKey + ":" + userId);
        List<TbItem> itemList = new ArrayList<>();
        for (String json : itemsJson) {
            itemList.add(JsonUtils.jsonToPojo(json,TbItem.class));
        }
        return itemList;
    }

    @Override
    public E3Result mergeItems(Long userId, List<TbItem> itemList) {
        for (TbItem tbItem : itemList) {
            addCart(userId, tbItem.getId(), tbItem.getNum());
        }
        return E3Result.ok();
    }

    @Override
    public E3Result deleteItem(Long userId, Long itemId) {
        jedisClient.hdel(redisCartKey + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result updateItem(Long userId, Long itemId, Integer num) {
        String itemJson = jedisClient.hget(redisCartKey + ":" + userId, itemId + "");
        TbItem tbItem = JsonUtils.jsonToPojo(itemJson, TbItem.class);
        tbItem.setNum(num);
        jedisClient.hset(redisCartKey + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        return E3Result.ok();
    }

    @Override
    public E3Result cleanCart(Long userId) {
        jedisClient.del(redisCartKey+":"+userId);
        return E3Result.ok();
    }


}
