package com.tank.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tank198435163.com
 */
@Configuration
public class PrepareConfig {

  @Bean(name = "restClient", destroyMethod = "close")
  public RestHighLevelClient initRestHighLevelClient() {
    return new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9201, "http")));
  }

}
