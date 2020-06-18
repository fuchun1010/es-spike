package com.tank;

import io.vavr.control.Try;
import lombok.val;
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

import java.util.Map;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

  @Test
  public void testRestClient() {
    val body = Try.of(() -> {
      GetRequest request = new GetRequest("order-comment", "1");
      return this.resetClient.get(request, RequestOptions.DEFAULT);
    }).onSuccess(GetResponse::getSource).getOrElseThrow(() -> new IllegalArgumentException("id为1的数据不存在"));
    Map<String, Object> data = body.getSource();
    Assert.assertEquals(data.size(), 5);
  }

  @Autowired
  @Qualifier("restClient")
  private RestHighLevelClient resetClient;

  private final String index = "order-comment";

  private final String type = "_doc";
}
