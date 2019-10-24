package cn.e3mall.item.listener;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class GenItemHtmlListener implements MessageListener{

    @Autowired
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message) {
        // 获取信息中的itemID
        TextMessage textMessage = (TextMessage) message;
        Long id = null;
        try {
            String idString = textMessage.getText();
            id = new Long(idString);

        } catch (JMSException e) {
            e.printStackTrace();
        }
        // 通过id查询商品信息以及商品详情
        TbItem tbItem = itemService.getItemById(id);
        Item item = new Item(tbItem);
        TbItemDesc itemDesc = itemService.getItemDescById(id);
        // 获取freemarker的配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        // 装配数据
        Map data =new HashMap();
        data.put("item",item);
        data.put("itemDesc",itemDesc);
        Writer out = null;
        try {
            // 加载模板文件
            Template template = configuration.getTemplate("item.ftl");
            // 配置输出目录及文件名
            out = new FileWriter(new File("E:\\Java\\upload\\e3html\\html\\"+id+".html"));
            // 生成静态页面
            template.process(data, out);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 关闭流
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
