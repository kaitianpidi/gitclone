package com.compass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ly
 * @date:2021/5/5 - 9:37
 */
@Configuration
@ConfigurationProperties(prefix = "gitClone")
@Data
public class GitCloneConfiguration {
    private String url;
    private String cmd;
    private String param;
    private String dir;
}
