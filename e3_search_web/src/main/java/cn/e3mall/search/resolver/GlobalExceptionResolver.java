package cn.e3mall.search.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        e.printStackTrace();
        logger.debug("系统出错了",e);
        logger.warn("系统出错了",e);
        logger.error("系统出错了",e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error/exception");
        modelAndView.addObject("message","系统发生异常，请稍后再试");
        return modelAndView;
    }
}
