package tech.yeez.investment.interceptor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fjtan
 */
@Configuration
@ConfigurationProperties(prefix = "cors")
public class WebConfigurer implements WebMvcConfigurer {

    private static String allowedDomain1;
    private static String allowedDomain2;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loginInterceptor).addPathPatterns("/system/**");
    }

    /**
     * cors
     * 
     * @return
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
//            .allowedOrigins(allowedDomain1, allowedDomain2)
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .allowedMethods("*")
            .maxAge(3600);

        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    public void setAllowedDomain1(String allowedDomain1) {
        WebConfigurer.allowedDomain1 = allowedDomain1;
    }

    public void setAllowedDomain2(String allowedDomain2) {
        WebConfigurer.allowedDomain2 = allowedDomain2;
    }

}
