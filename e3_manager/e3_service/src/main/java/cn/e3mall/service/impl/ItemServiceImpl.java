package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem getItemById(Long itemId) {
        TbItemExample tbItemExample = new TbItemExample();
        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        if (null != tbItems && tbItems.size() > 0){
            return tbItems.get(0);
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
        long id = IDUtils.genItemId();
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
        return E3Result.ok();
    }

    @Override
    public E3Result getItemDescById(long id){
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        return new E3Result(tbItemDesc);
    }
    @Override
    public E3Result queryItemDescById(long id){
        TbItem tbItem = tbItemMapper.selectByPrimaryKey(id);
        return E3Result.ok(tbItem);
    }


}
