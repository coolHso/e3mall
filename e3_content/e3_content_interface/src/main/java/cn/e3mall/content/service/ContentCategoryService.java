package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyTreeNode;
import cn.e3mall.common.utils.E3Result;

import java.util.List;

public interface ContentCategoryService {
    List<EasyTreeNode> getTreeNodeByParentId(long id);

    /**
     * @param parentId 插入结点的父结点id
     * @param name 插入节点的名字
     * @return 插入结点生成的id
     */
    E3Result createContentCategory(long parentId, String name);


    /** 更新结点信息
     * @param id 结点id
     * @param name 结点要更改的属性值
     * @return 更新结果
     */
    E3Result updateContentCategory(long id, String name);

    /** 删除结点
     * @param id 要删除结点的id
     */
    void deleteContentCategory(long id);
}
