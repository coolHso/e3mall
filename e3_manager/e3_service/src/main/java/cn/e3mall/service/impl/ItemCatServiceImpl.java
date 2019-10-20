package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyTreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Override
    public List<EasyTreeNode> getEasyTreeNodeByParentId(long parentId) {
        //根据parentid查询子节点列表
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = tbItemCatExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(tbItemCatExample);
        ArrayList<EasyTreeNode> list = new ArrayList<>();
        for (TbItemCat tbItemCat : tbItemCats) {
            EasyTreeNode node = new EasyTreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            list.add(node);
        }
        return list;
    }
}
