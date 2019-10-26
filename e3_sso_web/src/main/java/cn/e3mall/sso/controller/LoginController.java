package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 10642
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Value("${USER_TOKEN_COOKIE_KEY}")
    private String COOKIE_KEY;


    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model){
        model.addAttribute("redirect", redirect);
        return "login";
    }


    @RequestMapping("/user/login")
    @ResponseBody
    public E3Result login(String username, String password, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        // 登录
        E3Result result = loginService.login(username, password);
        // 设置cookie
        if (null != result.getData()){
            String token = result.getData().toString();
            CookieUtils.setCookie(httpServletRequest, httpServletResponse, COOKIE_KEY, token);
        }
        return  result;
    }

    /** 根据token查询redis并进行登录
     * @param token sessionId
     * @param callback 通过跨域请求返回数据中要调用的函数名称
     * @return 通过跨域请求返回数据中要调用的函数名称
     */
    @RequestMapping(value = "/user/token/{token}" , produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public Object loginByToken(@PathVariable String token, String callback){
        E3Result e3Result = loginService.loginByToken(token);
        // 判断是否为jsonp跨域请求
        if (StringUtils.isNotBlank(token)){
            return callback + "("+ JsonUtils.objectToJson(e3Result) + ");";
            // return mappingJacksonValue;
        }
        return JsonUtils.objectToJson(e3Result);
    }
}
