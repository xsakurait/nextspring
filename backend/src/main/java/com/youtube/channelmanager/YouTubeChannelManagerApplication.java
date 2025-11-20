package com.youtube.channelmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.youtube.channelmanager.mapper")
@EnableScheduling
public class YouTubeChannelManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouTubeChannelManagerApplication.class, args);
    }
}
