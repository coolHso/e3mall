package cn.e3mall.order.interceptor;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;
    @Autowired
    private CartService cartService;
    @Value("${LOGIN_URL}")
    private String loginUrl;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = CookieUtils.getCookieValue(httpServletRequest, "token", true);
        if (StringUtils.isNotBlank(token)) {
            //通过sso系统查询用户信息，查看是否过期
            E3Result e3Result = loginService.loginByToken(token);
            if (e3Result.getStatus() != 200){
            //    过期，需要登陆
                httpServletResponse.sendRedirect(loginUrl + "/page/login?redirect="+httpServletRequest.getRequestURL());
                return false;
            }else {
                //token正常
                TbUser user = (TbUser) e3Result.getData();
                httpServletRequest.setAttribute("user", user);
                //将cookie中的购物车信息合并到redis中
                String cart = CookieUtils.getCookieValue(httpServletRequest, "cart",true);
                if (StringUtils.isNotBlank(cart)){
                    List<TbItem> itemList = JsonUtils.jsonToList(cart, TbItem.class);
                    cartService.mergeItems(user.getId(), itemList);
                }
                return true;
            }
        }else {
            // 未登录
            httpServletResponse.sendRedirect(loginUrl + "/page/login?redirect="+httpServletRequest.getRequestURL());
            return false;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
