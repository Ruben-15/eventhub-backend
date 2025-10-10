package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String UPLOAD_DIR = "event-images";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        Path uploadPath = Paths.get(UPLOAD_DIR);
        String absolutePath = uploadPath.toFile().getAbsolutePath();
        
        // 🛑 FIX: Use replaceAll to convert Windows backslashes (\\) to forward slashes (/)
        // This resolves the image loading error and removes the editor warning.
        String normalizedPath = absolutePath.replaceAll("\\\\", "/");

        // Ensure the path ends with a slash for safety
        if (!normalizedPath.endsWith("/")) {
            normalizedPath += "/";
        }

        // Map the public URL path to the normalized file system path.
        registry.addResourceHandler("/api/events/event-images/**")
                .addResourceLocations("file:///" + normalizedPath);
    }
}