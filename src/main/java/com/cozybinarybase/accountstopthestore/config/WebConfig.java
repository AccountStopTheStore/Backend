package com.cozybinarybase.accountstopthestore.config;

import com.cozybinarybase.accountstopthestore.model.accountbook.dto.constants.converter.StringToTransactionTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new StringToTransactionTypeConverter());
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*") // 모든 도메인 허용
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("Authorization", "Content-Type", "User-Info")
        .exposedHeaders("Custom-Header", "User-Info")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
