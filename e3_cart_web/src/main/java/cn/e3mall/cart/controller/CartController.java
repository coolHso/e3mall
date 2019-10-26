package cn.e3mall.cart.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ItemService itemService;
    @Value("${CART_COOKIE_EXPIRE}")
    private Integer CART_COOKIE_EXPIRE;
    @Autowired
    private CartService cartService;

    @RequestMapping("/page/cart")
    public String showCart(HttpServletRequest request, HttpServletResponse response){
        List<TbItem> cartList = getCartByCookie(request);
        // if (cartList != null && cartList.size() > 0){
        //     String json = JsonUtils.objectToJson(cartList);
        // }
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null){
            //将cookie中的item与redis中的合并
            if (cartList != null && cartList.size()>0){
                cartService.mergeItems(user.getId(), cartList);
            }
            // 删除cookie
            CookieUtils.deleteCookie(request, response, "cart");
            // 从服务器获取购物车列表
            cartList = cartService.getItemsFromCart(user.getId());
        }
        request.setAttribute("cartList", cartList);
        return "cart";
    }


    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable Long itemId, Integer num, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        TbUser user = (TbUser) httpServletRequest.getAttribute("user");
        if (null != user){
            // 从redis中添加
            cartService.addCart(user.getId(), itemId, num);
            return "cartSuccess";
        }
        // 标明商品是否在购物车中
        boolean inCart = false;
        // 获取cookie
        List<TbItem> cart = getCartByCookie(httpServletRequest);
        // 判断该商品是否已经加入购物车
        for (TbItem tbItem : cart) {
            if (tbItem.getId().equals(itemId)){
                // 有则增加数量
                inCart = true;
                tbItem.setNum(tbItem.getNum() + num);
                break;
            }
        }
        if(!inCart) {
            // 没有加入购物车则加入购物车
            TbItem item = itemService.getItemById(itemId);
            // 可能有多张图片，只需设置一张即可
            String images = item.getImage();
            if(StringUtils.isNotBlank(images)){
                String image = images.split(",")[0];
                item.setImage(image);
            }
            item.setNum(num);
            cart.add(item);
        }
        // 将cart写入cookie中
        CookieUtils.setCookie(httpServletRequest, httpServletResponse, "cart", JsonUtils.objectToJson(cart), CART_COOKIE_EXPIRE, true);
        return "cartSuccess";
    }


    /** 通过cookie获取购物车中包含的商品
     * @param httpServletRequest request
     * @return cookie中对应的商品列表
     */
    private List<TbItem> getCartByCookie(HttpServletRequest httpServletRequest) {
        String cart = CookieUtils.getCookieValue(httpServletRequest, "cart", true);
        if (StringUtils.isBlank(cart)){
            return new ArrayList<>();
        }
        return JsonUtils.jsonToList(cart, TbItem.class);
    }

    @RequestMapping(value = "/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result changeCartNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request, HttpServletResponse response){
        TbUser user = (TbUser) request.getAttribute("user");
        if (user != null){
            //登录状态
            return cartService.updateItem(user.getId(), itemId, num);
        }
        //游客状态，从cookie中获得该item
        List<TbItem> itemList = getCartByCookie(request);
        for (TbItem tbItem : itemList) {
            if (itemId.equals(tbItem.getId())){
                tbItem.setNum(num);
                break;
            }
        }
        // 更新cookie
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), CART_COOKIE_EXPIRE, true);
        return E3Result.ok();
    }


    @RequestMapping(value = "/cart/delete/{itemId}")
    public String deleteItemFromCart(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response){
        TbUser user = (TbUser) request.getAttribute("user");
        if (null != user){
            // 登录状态
            cartService.deleteItem(user.getId(), itemId);
        }else{
            //游客状态
            if (itemId != null){
                List<TbItem> itemList = getCartByCookie(request);
                for (TbItem tbItem : itemList) {
                    if (itemId.equals(tbItem.getId())){
                        itemList.remove(tbItem);
                        break;
                    }
                }
                // 更新cookie
                CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(itemList), CART_COOKIE_EXPIRE, true);
            }
        }
        //返回购物车逻辑视图
        return "redirect:/page/cart.html";
    }

}
