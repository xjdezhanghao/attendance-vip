package com.attendance.framework.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat HTTP 连接器配置
 */
@Configuration
public class TomcatHttpConnectorConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Value("${http.port}")
    private int httpPort;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(httpPort); // 从配置文件读取HTTP回调端口
        connector.setSecure(false);
        connector.setRedirectPort(8443); // 重定向到HTTPS主端口

        factory.addAdditionalTomcatConnectors(connector);
    }
}