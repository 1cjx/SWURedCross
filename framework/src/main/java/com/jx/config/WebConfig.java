package com.jx.config;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jx.interceptor.ApiIdempotentInterceptor;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
      // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否允许cookie
                .allowCredentials(true)
                // 设置允许的请求方式
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
    @Bean//使用@Bean注入fastJsonHttpMessageConvert
    public HttpMessageConverter fastJsonHttpMessageConverters() {
        //1.需要定义一个Convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setDateFormat("yyyy-MM-dd");
        fastJsonConfig.setSerializeFilters(new ValueFilter() {
            Map<Class, Map<String, String>> dateFormatCache = new ConcurrentHashMap<>();
            @Override
            public Object process(Object obj, String key, Object value) {
                if (value instanceof Date) {
                    Class<?> objClass = obj.getClass();
                    if (dateFormatCache.get(objClass) == null) {
                        dateFormatCache.putIfAbsent(objClass, new HashMap<>());
                        Map<String, String> newFormatMap = dateFormatCache.get(objClass);
                        ReflectionUtils.doWithFields(obj.getClass(), (field) -> {
                            if (field.getType() == Date.class) {
                                JSONField annotation = field.getAnnotation(JSONField.class);
                                if (annotation != null) {
                                    String format = annotation.format();
                                    if (StringUtils.isNotBlank(format)) {
                                        newFormatMap.putIfAbsent(field.getName(), format);
                                    }
                                }
                            }
                        });
                    }

                    Map<String, String> dateFormatMap = dateFormatCache.get(objClass);
                    if (!dateFormatMap.isEmpty()) {
                        String format = dateFormatMap.get(key);
                        if(StringUtils.isNotBlank(format)) {
                            SimpleDateFormat sdf = new SimpleDateFormat(format);
                            return sdf.format(value);
                        }
                    }
                }
                return value;
            }
        });
        SerializeConfig.globalInstance.put(Long.class, ToStringSerializer.instance);
        SerializeConfig.globalInstance.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(SerializeConfig.globalInstance);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastConverter;
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(fastJsonHttpMessageConverters());
    }
    //幂等性拦截器
    @Bean
    public ApiIdempotentInterceptor apiIdempotentInterceptor() {
        return new ApiIdempotentInterceptor();
    }
    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiIdempotentInterceptor());
    }
}