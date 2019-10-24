package cn.e3mall.freemark;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FreeMrakTest {
    @Test
    public void testFreemark() throws IOException, TemplateException {
        // 1、创建一个模板文件

        // 2、创建一个COnfiguration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 3、设置模板文件保存的目录
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl"));
        // 4、模板文件的编码格式，一般为utf8
        configuration.setDefaultEncoding("utf-8");
        // 5、加载模板文件，创建模板对象
        Template template = configuration.getTemplate("hello.ftl");
        // 6、创建数据集，可以为pojo也可以为map，推荐map
        Map data = new HashMap<>();
        data.put("hello", "hello freemark!");
        // 7、创建一个Writer对象，指定输出文件的路径和文件名
        Writer out = new FileWriter(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl\\hello.txt"));
        // 8、生成静态页面
        template.process(data,out);
        // 9、关闭流
        out.close();
    }
    @Test
    public void testPojo() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap<>();
        Student student = new Student("1", "ah", 12);
        data.put("student",student);
        Writer out = new FileWriter(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl\\pojp.html"));
        template.process(data,out);
        out.close();
    }

    @Test
    public void testList() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap<>();
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(new Student("1", "ah1", 12));
        studentList.add(new Student("2", "ah2", 13));
        studentList.add(new Student("3", "ah3", 14));
        studentList.add(new Student("4", "ah4", 15));
        data.put("studentList",studentList);
        Writer out = new FileWriter(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl\\studentList.html"));
        template.process(data,out);
        out.close();
    }
    @Test
    public void testIf() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap<>();
        ArrayList<Student> studentList = new ArrayList<>();
        studentList.add(new Student("1", "ah1", 12));
        studentList.add(new Student("2", "ah2", 13));
        studentList.add(new Student("3", "ah3", 14));
        studentList.add(new Student("4", "ah4", 15));
        data.put("studentList",studentList);
        Writer out = new FileWriter(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl\\studentList.html"));
        template.process(data,out);
        out.close();
    }
    @Test
    public void testDate() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap<>();
        data.put("date",new Date());
        Writer out = new FileWriter(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl\\date.html"));
        template.process(data,out);
        out.close();
    }
    @Test
    public void testNull() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDirectoryForTemplateLoading(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl"));
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("hello.ftl");
        Map data = new HashMap<>();
        data.put("item",null);
        Writer out = new FileWriter(new File("E:\\Java\\project\\e3parent\\e3_item_web\\src\\main\\webapp\\WEB-INF\\ftl\\isNull.html"));
        template.process(data,out);
        out.close();
    }

}
