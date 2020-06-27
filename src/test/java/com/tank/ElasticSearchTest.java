package com.tank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tank.protocol.Order;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.val;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

/**
 * Unit test for simple App.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

  @Test
  public void testNodes() {
    val nodes = Stream.of(this.locations).map(node -> {
      val arr = node.split(":");
      return new Tuple2<String, Integer>(arr[0], Integer.parseInt(arr[1]));
    }).map(tuple -> new HttpHost(tuple._1(), tuple._2(), "http")).collect(Collectors.toList());
    val hosts = new HttpHost[nodes.size()];
    nodes.toArray(hosts);
    Assert.assertEquals(hosts.length, 2);
  }

  @Before
  public void initJsonObject() {
    this.jsonMapper = new ObjectMapper();
  }

  @Test
  public void testQueryDataWithId() throws Exception {
    val tmpOrder = Try.of(() -> {
      GetRequest request = new GetRequest(index, "1");
      return this.resetClient.get(request, RequestOptions.DEFAULT);
    }).onSuccess(GetResponse::getSource).map(GetResponse::getSource)
            .transform(d -> Try.of(() -> this.jsonMapper.writeValueAsString(d.get())))
            .transform(str -> Try.of(() -> this.jsonMapper.readValue(str.get(), Order.class)))
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


    BulkRequest prepareReq = Try.of(() -> this.jsonMapper.writeValueAsString(order))
            .transform(tryJsonStr -> {
              val request = new BulkRequest();
              return request.add(new IndexRequest(index).id(order.getOrderNo())
                      .source(tryJsonStr.get(), XContentType.JSON));
            });

    val created = Try.of(() -> this.resetClient.bulk(prepareReq, RequestOptions.DEFAULT))
            .map(BulkResponse::hasFailures).getOrElse(false);
    Assert.assertTrue(created);


  }

  @Test
  public void queryDataWithOneCondition() throws Exception {
    SearchRequest request = new SearchRequest();
    request.indices(this.index);
    val sourceBuilder = new SearchSourceBuilder();
    val matchBuilder = new MatchQueryBuilder("comment", "一条测试数据");
    sourceBuilder.query(matchBuilder);
    request.source(sourceBuilder);
    val response = Try.of(() -> this.resetClient.search(request, RequestOptions.DEFAULT))
            .getOrElseThrow(() -> new Exception("没找到"));
    SearchHits searchHits = response.getHits();
    Assert.assertEquals(searchHits.getHits().length, 1L);
    for (SearchHit searchHit : searchHits) {
      val data = searchHit.getSourceAsMap();
      val jsonStr = this.jsonMapper.writeValueAsString(data);
      Assert.assertNotNull(jsonStr);
      Assert.assertTrue(jsonStr.trim().length() > 0);
      System.out.println(jsonStr);
    }

  }

  @Test
  public void queryDataWithBooleanFilter() throws Exception {
    val request = new SearchRequest();
    val searchSourceBuilder = new SearchSourceBuilder();
    request.source(searchSourceBuilder);

    val boolQueryBuilder = QueryBuilders.boolQuery();
    val orderNoMatcher = new MatchQueryBuilder("orderNo", "1");
    val snapshotIdMatcher = new MatchQueryBuilder("items.snapshotId", "s0001");
    val allQueryBuilder = boolQueryBuilder.must(orderNoMatcher).must(snapshotIdMatcher);
    searchSourceBuilder.query(allQueryBuilder);

    val response = this.resetClient.search(request, RequestOptions.DEFAULT);
    Assert.assertEquals(response.getHits().getHits().length, 1);
    for (SearchHit searchHit : response.getHits()) {
      val data = searchHit.getSourceAsMap();
      val jsonStr = this.jsonMapper.writeValueAsString(data);
      Assert.assertNotNull(jsonStr);
      Assert.assertTrue(jsonStr.trim().length() > 0);
      System.out.println(jsonStr);
    }

  }

  @Autowired
  @Qualifier("restClient")
  private RestHighLevelClient resetClient;

  @Autowired
  private Environment environment;

  private final String index = "order-comment";

  private ObjectMapper jsonMapper = null;

  @Value("${spring.elasticsearch.rest.uris}")
  private String[] locations;
}
