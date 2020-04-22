package app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class RedirectConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/berth/**")
                .addResourceLocations("file:./web-content/")
                .setCachePeriod(3600)
                .setCacheControl(CacheControl.maxAge(3600, TimeUnit.SECONDS));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/berth");
        registry.addViewController("/berth/").setViewName("forward:/berth");
        registry.addViewController("/berth").setViewName("forward:/berth/index.html");
    }
}
