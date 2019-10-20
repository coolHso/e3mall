package cn.e3mall.fast;

import cn.e3mall.common.utils.FastDFSClient;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.IOException;

public class FastDfsTest {

    @Test
    public void fastFsdTest() throws IOException, MyException {
        //创建一个配置文件，文件名任意，内容就是tracker服务器的地址
        //使用全局对象加载配置文件
        ClientGlobal.init("E:\\Java\\project\\e3parent\\e3_web\\src\\main\\resources\\client.conf");
        //创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过trackerClient对象获取一个TrackerServer对象
        TrackerServer connection = trackerClient.getConnection();
        //创建一个StorageServer的引用，可以为空
        StorageServer storageServer = null;
        //创建一个storageClient
        StorageClient storageClient = new StorageClient(connection, storageServer);
        //通过StorageClient上传文件
        String[] jpgs = storageClient.upload_file("C:\\Users\\10642\\Desktop\\素材\\图\\B\\black-and-white-dark-curve-forest.jpg", "jpg", null);
        //获取文件的访问路径
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }
    }

    @Test
    public void testFastFdsUtil() throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("E:\\Java\\project\\e3parent\\e3_web\\src\\main\\resources\\client.conf");
        String url = fastDFSClient.uploadFile("C:\\Users\\10642\\Desktop\\素材\\图\\A\\22-1F225225440416.jpg");
        System.out.println(url);
    }

}
