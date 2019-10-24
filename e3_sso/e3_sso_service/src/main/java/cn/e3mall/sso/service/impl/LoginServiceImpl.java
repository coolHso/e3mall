package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${TOKEN_EXPIRE}")
    private Integer TOKEN_EXPIRE;


    @Override
    public E3Result login(String username, String password) {
         // 1、检验账号密码是否正确
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        if (StringUtils.isNotBlank(username)){
            criteria.andUsernameEqualTo(username);
        }
        List<TbUser> tbUsers = userMapper.selectByExample(tbUserExample);
        // 2、取用户信息
        if (tbUsers == null || tbUsers.size() == 0){
            return E3Result.build(400, "账号或密码错误！");
        }
        TbUser user = tbUsers.get(0);
        // 2.1、判断密码是否正确
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return E3Result.build(400, "账号或密码错误！");
        }
         // 3、将用户信息写入redis，key：token，value：用户信息
        String token = UUID.randomUUID().toString();
        user.setPassword(null);
        jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
        // 4、设置token过期时间
        jedisClient.expire("SESSION:"+token, TOKEN_EXPIRE);
         // 6、返回token //
        return E3Result.ok(token);
    }


    /** 根据token登录
     * @param token sessionId
     * @return 是否登录成功
     */
    @Override
    public E3Result loginByToken(String token){
        //查询redis是否有该token
        String userJson = jedisClient.get("SESSION:" + token);
        // 在redis中查询命中
        if (StringUtils.isNotBlank(userJson)){
            // 更新过期时间
            jedisClient.expire("SESSION:"+token, TOKEN_EXPIRE);
            TbUser tbUser = JsonUtils.jsonToPojo(userJson, TbUser.class);
            return E3Result.ok(tbUser);
        }
        return E3Result.build(400,"token登录失败");
    }


}
