package com.example.lostandfoundbackend.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.lostandfoundbackend.entity.User;
import com.example.lostandfoundbackend.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Tao
 */
@Component
public class TokenUtils {

    /**
     * 用于后面静态方法中使用(静态方法中只能使用静态变量)
     */
    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * 生成token
     *
     * @return 一个token
     */
    public static String createToken(String userId, String sign) {
        //withAudience 方法在 JWT（JSON Web Token）的创建过程中用于指定令牌的受众
        //可以存储用户信息，如用户ID、用户名等，以便在验证和授权过程中进行身份验证和授权。
        //此处将user id 保存到token里面，作为载荷，可以有多个，用‘，’隔开
        return JWT.create().withAudience(userId)
                //两小时后过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                //使用password作为token密钥
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 使用token获取当前登录的用户信息(重要)
     * @return user对象
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
