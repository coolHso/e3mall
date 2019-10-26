package cn.e3mall.order.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${ORDER_ID_INCR_KEY}")
    private String orderIdCreator;
    @Value("${ORDER_ID_START}")
    private String orderIdStart;
    @Value("${ORDER_DETAIL_ID_INCR_KEY}")
    private String orderDetailIdCreator;

    @Override
    public E3Result createOrder(OrderInfo orderInfo) {
        //生成订单号
        if (jedisClient.exists(orderIdCreator))
        {
            jedisClient.set(orderIdCreator, orderIdStart);
        }
        String orderId = jedisClient.incr(orderIdCreator).toString();
        //补全orderInfo的属性
        orderInfo.setOrderId(orderId);
        orderInfo.setStatus(1);
        Date date = new Date();
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        //插入订单表
        orderMapper.insert(orderInfo);
        //插入订单明细表
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem orderItem : orderItems) {
            orderItem.setId(jedisClient.incr(orderDetailIdCreator).toString());
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
        }
        // 插入订单物流表
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        orderShipping.setOrderId(orderId);
        orderShippingMapper.insert(orderShipping);
        return E3Result.ok(orderId);
    }
}
