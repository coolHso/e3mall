package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyTreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EasyTreeNode> getTreeNodeByParentId(long parentId) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> tbContentCategories = contentCategoryMapper.selectByExample(tbContentCategoryExample);
        List<EasyTreeNode> list = new ArrayList<>();
        for (TbContentCategory tbContentCategory : tbContentCategories) {
            EasyTreeNode node = new EasyTreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            list.add(node);
        }
        return list;
    }

    @Override
    public E3Result createContentCategory(long parentId, String name) {
        //创建节点，补全内容
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setIsParent(false);
        tbContentCategory.setName(name);
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setStatus(1);
        Date date = new Date();
        tbContentCategory.setUpdated(date);
        tbContentCategory.setCreated(date);
        //插入结点，获取id
        int nodeId = contentCategoryMapper.insert(tbContentCategory);
        //如父结点的isParent为0则设为1
        TbContentCategory parent = new TbContentCategory();
        parent.setId(parentId);
        parent.setIsParent(true);
        contentCategoryMapper.updateByPrimaryKeySelective(parent);
        //返回更新结果
        return E3Result.ok(tbContentCategory);
    }

    @Override
    public E3Result updateContentCategory(long id, String name) {
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andIdEqualTo(id);
        List<TbContentCategory> tbContentCategories = contentCategoryMapper.selectByExample(tbContentCategoryExample);
        if (tbContentCategories != null && tbContentCategories.size() > 0){
            TbContentCategory tbContentCategory = tbContentCategories.get(0);
            tbContentCategory.setName(name);
            contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
            return E3Result.ok(tbContentCategory);
        }
        return null;
    }

    @Override
    public void deleteContentCategory(long id) {

        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(id);
        int count= contentCategoryMapper.countByExample(tbContentCategoryExample);
        if(count == 1){
            //要删除的结点无兄弟结点，将父结点的isparent设置为false
            TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
            TbContentCategory parent = new TbContentCategory();
            parent.setId(tbContentCategory.getParentId());
            parent.setIsParent(false);
            parent.setUpdated(new Date());
            contentCategoryMapper.updateByPrimaryKeySelective(parent);
        }
        //删除结点
        contentCategoryMapper.deleteByPrimaryKey(id);
    }
}
