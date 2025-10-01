package com.foodordering.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure WebJars
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false);

        // Configure static resources
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(false);

        // Configure CSS and JS resources
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .resourceChain(false);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .resourceChain(false);

        // Configure images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .resourceChain(false);
    }
}
