package com.tank;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

  @Test
  public void testRestClient() {
    Assert.assertNotNull(resetClient);
    GetRequest request = new GetRequest("order-comment", "_doc", "1");
    try {
      GetResponse response = this.resetClient.get(request, RequestOptions.DEFAULT);
      System.out.println(response.getSource().size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Autowired
  @Qualifier("restClient")
  private RestHighLevelClient resetClient;

  private final String index = "order-comment";

  private final String type = "_doc";
}
