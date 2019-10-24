package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result checkData(String param, int type) {
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        //用户名1，手机号2，邮箱3
        if (type == 1){
            criteria.andUsernameEqualTo(param);
        }else if(type == 2){
            criteria.andPhoneEqualTo(param);
        }else if(type == 3){
            criteria.andEmailEqualTo(param);
        }else {
            return E3Result.build(400,"类型错误");
        }
        List<TbUser> tbUsers = tbUserMapper.selectByExample(tbUserExample);
        if (tbUsers != null && tbUsers.size()>0){
            return E3Result.ok(false);
        }
        return E3Result.ok(true);
    }

    @Override
    public E3Result register(TbUser user) {
        //校验数据
        if (StringUtils.isBlank(user.getUsername())  || StringUtils.isBlank(user.getPhone())){
            return E3Result.build(400,"数据不完整，注册失败");
        }
        E3Result e3Result = checkData(user.getUsername(), 1);
        if (!(boolean)e3Result.getData()){
            return E3Result.build(400,"此用户名已经被占用");
        }
        e3Result = checkData(user.getPhone(), 2);
        if (!(boolean)e3Result.getData()){
            return E3Result.build(400,"此手机号已经被占用");
        }
        //补全注册信息
        Date date = new Date();
        user.setCreated(date);
        user.setUpdated(date);
        //对密码md5加密
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        //插入数据库
        tbUserMapper.insert(user);
        return E3Result.ok();
    }
}
