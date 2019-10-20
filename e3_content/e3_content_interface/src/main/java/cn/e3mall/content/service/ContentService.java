package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

/**
 * @author 10642
 */
public interface ContentService {
    EasyUIDataGridResult getContentsByCategoryId(long categoryId, int page,int rows);

    List<TbContent> getTbcontentsByCid(long cid);

    E3Result saveContent(TbContent tbContent);

    E3Result updateContentById(TbContent tbContent);

    E3Result deleteContentsById(long[] ids);

}
