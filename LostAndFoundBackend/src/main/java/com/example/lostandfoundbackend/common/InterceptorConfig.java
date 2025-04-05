package com.example.lostandfoundbackend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器配置
 *
 * @Author Tao
 * @Date 2025/4/5 12:02
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    /**
     * 是WebMvcConfigurationSupport中空方法，需要自己重写
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //自定义拦截器规则，拦截除login和register之外的所有请求
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/register");
        super.addInterceptors(registry);
    }

    /**
     * 注册拦截器`
     *
     * @return JWT拦截器`
     */
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }


}
