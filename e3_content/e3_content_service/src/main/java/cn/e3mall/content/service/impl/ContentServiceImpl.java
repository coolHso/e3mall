package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_AD_CONTENT}")
    private String content_list;

    @Override
    public EasyUIDataGridResult getContentsByCategoryId(long categoryId,int page,int rows) {
        //通过pageHelper插件设置页数
        PageHelper.startPage(page,rows);
        //通过example查询
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        //List<TbContent> tbContents = contentMapper.selectByExample(tbContentExample);
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(tbContentExample);
        //创建easyuiDatagrid结果对象
        EasyUIDataGridResult gridResult = new EasyUIDataGridResult();
        gridResult.setRows(tbContents);
        //分页信息对象
        PageInfo<TbContent> tbContentPageInfo = new PageInfo<>(tbContents);
        gridResult.setTotal(tbContentPageInfo.getTotal());
        return gridResult;
    }

    @Override
    public List<TbContent> getTbcontentsByCid(long cid) {
        //从redis中获取数据
        try {
            String json = jedisClient.hget(content_list, Long.toString(cid));
            if (StringUtils.isNotBlank(json)){
                return JsonUtils.jsonToList(json, TbContent.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // redis中没有该缓存，则从数据库中查找
        TbContentExample tbContentExample = new TbContentExample();
        TbContentExample.Criteria criteria = tbContentExample.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> tbContents = contentMapper.selectByExample(tbContentExample);
        // 加载进缓存中
        jedisClient.hset(content_list,Long.toString(cid),JsonUtils.objectToJson(tbContents));
        return tbContents;
    }

    @Override
    public E3Result saveContent(TbContent tbContent) {
        Date date = new Date();
        tbContent.setCreated(date);
        tbContent.setUpdated(date);
        contentMapper.insert(tbContent);
        //同步缓存，只需将对应的哈希的键删除即可
        jedisClient.hdel(content_list,tbContent.getCategoryId().toString());
        return E3Result.ok();
    }

    @Override
    public E3Result updateContentById(TbContent tbContent) {
        tbContent.setUpdated(new Date());
        contentMapper.updateByPrimaryKeySelective(tbContent);
        jedisClient.hdel(content_list,tbContent.getCategoryId().toString());
        return E3Result.ok();
    }


    @Override
    public E3Result deleteContentsById(long[] ids) {
        for (long id : ids) {
            contentMapper.deleteByPrimaryKey(id);
        }
        return E3Result.ok();
    }
}
