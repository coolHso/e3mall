package redisTest;

import cn.e3mall.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;

public class RedisConnect {
    @Test
    public void poolTest() throws  Exception{
        JedisPool jedisPool = new JedisPool("192.168.1.131", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("test","123");
        String test = jedis.get("test");
        System.out.println(test);
        jedis.close();
        jedisPool.close();
    }
    //测试连接redis集群
    @Test
    public void clusterTest() throws Exception{
        HashSet<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.1.131",7001));
        nodes.add(new HostAndPort("192.168.1.131",7002));
        nodes.add(new HostAndPort("192.168.1.131",7003));
        nodes.add(new HostAndPort("192.168.1.131",7004));
        nodes.add(new HostAndPort("192.168.1.131",7005));
        nodes.add(new HostAndPort("192.168.1.131",7006));
        //创建一个jedisCluster对象，其所要求的参数为若干个HostAndPort组成的Set集合，对应着集群中的各个redis结点
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("test","123");
        System.out.println(jedisCluster.get("test"));
        jedisCluster.close();
    }

    @Test
    public void clientTest() throws Exception{
        ApplicationContext applicationContext  = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
        jedisClient.set("test","123");
        System.out.println(jedisClient.get("test"));
    }

}
