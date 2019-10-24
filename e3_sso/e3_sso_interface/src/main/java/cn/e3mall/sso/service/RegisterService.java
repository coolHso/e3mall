package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * @author 10642
 */
public interface RegisterService {
    /** 用于检查用户注册时是否有信息与数据库重复
     * @param param 需要检查的数据
     * @param type 检查的字段，1为用户名，2为手机号，3为邮箱
     * @return 若重复则返回false，反则返回true
     */
    E3Result checkData(String param, int type);


    /** 完成注册功能
     * @param user 用户信息
     * @return 注册是否成功
     */
    E3Result register(TbUser user);
}
