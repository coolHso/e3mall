package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyTreeNode;

import java.util.List;

public interface ItemCatService  {

    /**
     * @param parentId 父结点的id
     * @return 对应父结点的子结点列表
     */
    List<EasyTreeNode> getEasyTreeNodeByParentId(long parentId);




}
