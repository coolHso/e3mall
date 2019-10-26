package cn.e3mall.order.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order/order-cart")
    public String showOrderCart(HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        //根据id取购物车列表
        List<TbItem> cartList = cartService.getItemsFromCart(user.getId());
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

    @RequestMapping(value = "/order/create")
    public String createOrder(OrderInfo orderInfo, HttpServletRequest request){
        TbUser user = (TbUser) request.getAttribute("user");
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());
        //生成订单
        E3Result order = orderService.createOrder(orderInfo);
        //如果成功,清空购物车
        if (order.getStatus() == 200) {
            cartService.cleanCart(user.getId());
        }
        //将订单号传回页面
        request.setAttribute("orderId", order.getData());
        request.setAttribute("payment", orderInfo.getPayment());
        return "success";
    }

}
