package com.houchaojie.blog.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 侯超杰
 */
@Slf4j
public class SecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws IOException {
        log.info("SecurityInterceptor...preHandle...");
        //这里可以根据session的用户来判断角色的权限，根据权限来转发不同的页面
        if(request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)  {
        log.info("SecurityInterceptor.....Controller执行之后");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e)  {
        log.info("SecurityInterceptor.....释放资源");
    }

}
