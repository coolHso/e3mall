package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

public interface ItemService {

    /** 通过id查询商品对象
     * @param itemId 商品id
     * @return 商品对象
     */
    TbItem getItemById(Long itemId);

    /** 通过当前页数和每页记录数进行分页，返回分页对象
     * @param page 当前页数
     * @param rows  每页的记录数
     * @return 分页对象
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    /** 将商品插入商品表
     * @param tbItem 商品条目的信息
     * @param desc  商品条目的描述
     * @return  是否插入成功
     */
    E3Result addItem(TbItem tbItem, String desc);

    /** 通过id查询商品的描述信息
     * @param id 商品id
     * @return 查找商品的状态
     */
    E3Result getItemDescById(long id);

    E3Result queryItemDescById(long id);
}
