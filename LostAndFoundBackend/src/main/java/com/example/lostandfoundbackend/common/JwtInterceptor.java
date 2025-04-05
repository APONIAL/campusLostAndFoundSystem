package com.example.lostandfoundbackend.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.exception.ServiceException;
import com.example.lostandfoundbackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 *
 * @Author Tao
 * @Date 2025/4/5 9:30
 * @Version 1.0
 */
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //header里面传递过来的参数{
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            //url参数 ？token=xxxx
            token = request.getParameter("token");
        }
        //如果不是映射到方法，直接通过
        if(handler instanceof HandlerMethod){
            AuthAccess annotation = ((HandlerMethod) handler).getMethodAnnotation(AuthAccess.class);
            if (annotation != null){
                return true;
            }
        }
        //执行认证
        if (StrUtil.isBlank(token)) {
            //抛出异常，被全局异常处理类处理
            throw new ServiceException(Constants.CODE_401, "无token，请重新登录");
        }
        //获取 token 中的 user id
        String userId;
        try {
            //JWT.decode(token)解码JWT token,拿到Audience中第一个元素(user id)
            userId = JWT.decode(token).getAudience().get(0);
        } catch (Exception e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }

        User user = userService.getById(Integer.valueOf(userId));

        if (user == null) {
            throw new ServiceException(Constants.CODE_401, "token携带的用户不存在，请重新登录");
        }
        //通过用户密码加密后生成一个验证器，与前端传递的token进行对比
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            //验证token
            //如果验证不通过，会抛出异常
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401, "token验证失败，请重新登录");
        }
        return true;
    }
}