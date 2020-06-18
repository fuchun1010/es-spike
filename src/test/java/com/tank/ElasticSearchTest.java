package com.tank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tank.protocol.Order;
import io.vavr.control.Try;
import lombok.val;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

  @Before
  public void initJsonObject() {
    this.objectMapper = new ObjectMapper();
  }

  @Test
  public void testRestClient() throws Exception {
//    val body = Try.of(() -> {
//      GetRequest request = new GetRequest("order-comment", "1");
//      return this.resetClient.get(request, RequestOptions.DEFAULT);
//    }).onSuccess(GetResponse::getSource).getOrElseThrow(() -> new IllegalArgumentException("id为1的数据不存在"));
//    Map<String, Object> data = body.getSource();
//    Assert.assertEquals(data.size(), 5);
//    String empty = "-";
//    String jsonStr = Try.of(() -> this.objectMapper.writeValueAsString(data)).getOrElse(empty);
//    if (empty.equals(jsonStr)) {
//      throw new IllegalArgumentException("数据不对");
//    }
//    Order order = Try.of(() -> this.objectMapper.readValue(jsonStr, Order.class))
//            .getOrElseThrow(() -> new Exception("json to Object Exception"));
//    Assert.assertEquals(order.getItems().size(), 1);

    val tmpOrder = Try.of(() -> {
      GetRequest request = new GetRequest("order-comment", "1");
      return this.resetClient.get(request, RequestOptions.DEFAULT);
    }).onSuccess(GetResponse::getSource).map(GetResponse::getSource)
            .transform(d -> Try.of(() -> this.objectMapper.writeValueAsString(d.get())))
            .transform(str -> Try.of(() -> this.objectMapper.readValue(str.get(), Order.class)))
            .getOrElseThrow(() -> new IllegalArgumentException("id为1的不存在"));
    Assert.assertEquals(tmpOrder.getItems().size(), 1);

  }

  @Autowired
  @Qualifier("restClient")
  private RestHighLevelClient resetClient;

  private final String index = "order-comment";

  private final String type = "_doc";

  private ObjectMapper objectMapper = null;
}
