package com.attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author june
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class AttendanceApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(AttendanceApplication.class, args);
        System.out.println("山东机场信息考勤绩效平台启动成功！！！");
    }
}