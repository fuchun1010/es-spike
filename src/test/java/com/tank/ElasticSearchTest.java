package com.tank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tank.protocol.Order;
import io.vavr.control.Try;
import lombok.val;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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
  public void testQueryDataWithId() throws Exception {
    val tmpOrder = Try.of(() -> {
      GetRequest request = new GetRequest(index, "1");
      return this.resetClient.get(request, RequestOptions.DEFAULT);
    }).onSuccess(GetResponse::getSource).map(GetResponse::getSource)
            .transform(d -> Try.of(() -> this.objectMapper.writeValueAsString(d.get())))
            .transform(str -> Try.of(() -> this.objectMapper.readValue(str.get(), Order.class)))
            .getOrElseThrow(() -> new IllegalArgumentException("id为1的不存在"));
    Assert.assertEquals(tmpOrder.getItems().size(), 1);
  }

  @Test
  public void createData() {
    val order = new Order();
    order.setComment("一条测试数据").setOrderNo("2").setOrgCode("0087").setScore(5);

    Order.ItemsBean item1 = new Order.ItemsBean();
    item1.setWeight(2).setSnapshotId("s0001");

    Order.ItemsBean item2 = new Order.ItemsBean();
    item2.setWeight(3).setSnapshotId("s0002");

    order.getItems().add(item1);
    order.getItems().add(item2);


    BulkRequest prepareReq = Try.of(() -> this.objectMapper.writeValueAsString(order))
            .transform(tryJsonStr -> {
              val request = new BulkRequest();
              return request.add(new IndexRequest(index).id(order.getOrderNo())
                      .source(tryJsonStr.get(), XContentType.JSON));
            });

    val created = Try.of(() -> this.resetClient.bulk(prepareReq, RequestOptions.DEFAULT))
            .map(BulkResponse::hasFailures).getOrElse(false);
    Assert.assertTrue(created);


  }

  

  @Autowired
  @Qualifier("restClient")
  private RestHighLevelClient resetClient;

  private final String index = "order-comment";


  private ObjectMapper objectMapper = null;
}
