package com.shopping;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(registry, "category-images");
        exposeDirectory(registry, "brand-logos");
        exposeDirectory(registry, "product-images");
    }

    private void exposeDirectory(ResourceHandlerRegistry registry, String resourceName) {
        Path resourceDir = Paths.get(resourceName);
        String resourcePath = resourceDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + resourceName + "/**")
                .addResourceLocations("file:/" + resourcePath + "/");
    }
}