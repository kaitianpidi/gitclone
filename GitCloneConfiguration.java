package com.compass.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ｙｍｌ配置文件，增加如下配置
 
    #gitub克隆配置
    gitClone:
      cmd: cmd
      param: "/C"
      url:  https://gitee.com/mingxin400/aop.git
    #　url:  https://gitee.com/zycgit/hasor.git
      dir: d:/tmp
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
