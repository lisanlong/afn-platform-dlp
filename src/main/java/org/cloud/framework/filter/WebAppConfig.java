package org.cloud.framework.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter{
    public WebAppConfig(){
        super();
    }
    @Bean
    public HandlerInterceptor getMyInterceptor(){
        return new LoginFilter();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/login**").excludePathPatterns("/web**")
        .excludePathPatterns("/error");
        super.addInterceptors(registry);
    }

}

