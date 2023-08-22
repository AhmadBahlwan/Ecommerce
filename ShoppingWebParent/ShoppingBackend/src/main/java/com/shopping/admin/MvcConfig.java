package com.shopping.admin;

import com.shopping.admin.paging.PagingAndSortingArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        configureResourceHandler(registry, "user-photos");
        configureResourceHandler(registry, "category-images");
        configureResourceHandler(registry, "brand-logos");
        configureResourceHandler(registry, "product-images");
        configureResourceHandler(registry, "site-logo");
    }

    private void configureResourceHandler(ResourceHandlerRegistry registry, String resourceName) {
        Path resourceDir = Paths.get(resourceName);
        String resourcePath = resourceDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + resourceName + "/**")
                .addResourceLocations("file:/" + resourcePath + "/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PagingAndSortingArgumentResolver());
    }
}


