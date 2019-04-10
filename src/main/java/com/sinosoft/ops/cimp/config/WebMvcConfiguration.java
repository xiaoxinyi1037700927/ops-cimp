package com.sinosoft.ops.cimp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.ops.cimp.interceptor.WebContextInterceptor;
import com.vip.vjtools.vjkit.text.Charsets;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 增加Spring MVC的拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new WebContextInterceptor());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        messageConverter.setDefaultCharset(Charsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND);

        objectMapper.setDateFormat(simpleDateFormat);
        messageConverter.setObjectMapper(objectMapper);

        //设置中文编码格式
        List<MediaType> list = new ArrayList<>(2);
        list.add(MediaType.APPLICATION_JSON_UTF8);
        messageConverter.setSupportedMediaTypes(list);
        converters.add(messageConverter);
    }
}
