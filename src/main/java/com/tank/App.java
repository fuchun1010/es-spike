package com.tank;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author tank198435163.com
 */
@SpringBootApplication
@NacosPropertySource(dataId = "es-spike.yaml")
public class App {
  public static void main(final String[] args) {
    SpringApplication.run(App.class, args);
  }
}
