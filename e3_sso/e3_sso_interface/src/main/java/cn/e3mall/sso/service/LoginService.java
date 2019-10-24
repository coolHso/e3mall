package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * @author 10642
 */
public interface LoginService {

    /** 用户登录处理
     * 1、检验账号密码是否正确
     * 2、不正确返回登录失败，正确则生成token
     * 3、将用户信息写入redis，key：token，value：用户信息
     * 4、设置token过期时间
     * 6、返回token
     * @param username 用户名
     * @param password 密码
     * @return E3Result，包含token信息
     */
    E3Result login(String username, String password);

    E3Result loginByToken(String token);


}
