package com.attendance.framework.config; // 或者您项目中的其他配置包路径

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

/**
 * RestTemplate 配置类
 * 用于向Spring容器中注册一个RestTemplate Bean。
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建并配置一个RestTemplate Bean。
     * RestTemplate用于在应用程序中发送同步的HTTP请求。
     *
     * @param builder RestTemplateBuilder，由Spring Boot自动配置，可用于自定义RestTemplate
     * @return 配置好的RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 您可以在这里使用 RestTemplateBuilder 来进行更详细的配置，
        // 例如设置连接超时、读取超时、自定义消息转换器、错误处理器等。

        // 从 ZmhConfig 中获取超时配置（如果需要的话，需要注入ZmhConfig）
        // ZmhConfig zmhConfig = new ZmhConfig(); // 实际项目中应通过依赖注入获取

        // 示例：设置连接和读取超时 (假设 ZmhConfig 已被正确注入或值已获取)
        // int connectTimeout = zmhConfig.getConnectTimeout(); // 假设为 5000ms
        // int readTimeout = zmhConfig.getReadTimeout();    // 假设为 10000ms

        // 如果不注入ZmhConfig，可以直接使用硬编码的默认值或从builder获取
        return builder
                // .setConnectTimeout(Duration.ofMillis(connectTimeout)) // 例如: 5秒连接超时
                // .setReadTimeout(Duration.ofMillis(readTimeout))       // 例如: 10秒读取超时
                .setConnectTimeout(Duration.ofSeconds(10)) // 示例：10秒连接超时
                .setReadTimeout(Duration.ofSeconds(30))    // 示例：30秒读取超时
                .build();

        // 或者，如果您不需要复杂的配置，可以直接返回一个新的实例：
        // return new RestTemplate();
        // 但使用 RestTemplateBuilder 是Spring Boot推荐的方式，因为它允许更多的自动配置和自定义。
    }

    // 如果您还想配置 ObjectMapper Bean（尽管Spring Boot通常会自动配置一个），
    // 也可以在这里定义，或者确保它在其他地方被正确配置和注入。
    // @Bean
    // public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
    //     com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    //     // 在这里可以对 objectMapper 进行自定义配置，例如日期格式、忽略未知属性等
    //     // objectMapper.disable(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    //     return objectMapper;
    // }
}
