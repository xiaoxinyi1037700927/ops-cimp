package com.sinosoft.ops.cimp.config;

import com.sinosoft.ops.cimp.interceptor.WebContextAuthenticationInterceptor;
import com.sinosoft.ops.cimp.interceptor.WebContextInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Order(0)
@Configuration
public class WebContextInterceptorConfig extends WebMvcConfigurerAdapter {

    public WebContextInterceptorConfig() {
        super();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/");

        registry.addResourceHandler("/templates/**")
                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/templates/");

        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebContextInterceptor()).addPathPatterns("/**");

        //LoginInterceptor初始化上下文
        registry.addInterceptor(new WebContextAuthenticationInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
