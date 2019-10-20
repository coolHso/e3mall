package cn.e3mall.search.mapper;

import cn.e3mall.common.pojo.ItemSearch;

import java.util.List;

/**
 * @author 10642
 */
public interface ItemMapper {
    /**查询数据库中所有item
     * @return 查询结果列表
     */
    List<ItemSearch> getItemList();
}
