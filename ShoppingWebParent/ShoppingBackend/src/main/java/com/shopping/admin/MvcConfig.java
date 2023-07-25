package com.shopping.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig  implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String userImagesName = "user-photos";
        Path userPhotosDir = Paths.get(userImagesName);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + userImagesName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImagesDirName = "category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);
        String categoryPhotosPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/category-images/**")
                .addResourceLocations("file:/" + categoryPhotosPath + "/");
    }
}
