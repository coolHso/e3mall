package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

public interface CartService {
    E3Result addCart(Long userId, Long itemId, Integer num);
    List<TbItem> getItemsFromCart(Long userId);
    E3Result mergeItems(Long userId, List<TbItem> itemList);
    E3Result deleteItem(Long userId, Long itemId);
    E3Result updateItem(Long userId, Long itemId, Integer num);
    E3Result cleanCart(Long userId);
}
