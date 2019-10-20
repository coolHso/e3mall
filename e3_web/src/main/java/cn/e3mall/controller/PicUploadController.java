package cn.e3mall.controller;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PicUploadController {

    @Value("${IMG_SERVER_URL}")
    private String imgServerURL;


    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile){
        //截取文件的后缀名
        String filename = uploadFile.getOriginalFilename();
        String type = filename.substring(filename.lastIndexOf('.') + 1);
        //返回的map集合
        Map map = new HashMap<>(2);
        try {
            //上传图片
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), type);
        //    拼接完整的url
             url = imgServerURL + url;
            map.put("error",0);
            map.put("url",url);
            return JsonUtils.objectToJson(map);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error","1");
            map.put("message","上传图片失败");
            return JsonUtils.objectToJson(map);
        }
    }


}
