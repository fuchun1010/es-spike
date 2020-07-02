package com.tank.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.google.common.collect.Maps;
import com.tank.protocol.condition.ConditionContainer;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author tank198435163.com
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

  @GetMapping("/cpuInfo")
  public ResponseEntity<Map<String, Object>> cpuInfo() {
    Map<String, Object> body = Maps.newConcurrentMap();
    val cpuNumber = Runtime.getRuntime().availableProcessors();
    body.put("cores", cpuNumber);
    return ResponseEntity.ok(body);
  }

  @PostMapping("/query")
  public ResponseEntity<Map<String, Object>> query(@RequestBody ConditionContainer container) {
    Map<String, Object> body = Maps.newConcurrentMap();
    BoolQueryBuilder boolQueryBuilder = container.parse();
    body.put("hello", "2");
    return ResponseEntity.ok(body);
  }

  @GetMapping("/name")
  public ResponseEntity<Map<String, String>> name() {
    Map<String, String> body = Maps.newHashMap();
    body.put("name", this.name);
    return ResponseEntity.ok(body);
  }

  //auto refreshed must here
  @NacosValue(value = "${test.name}", autoRefreshed = true)
  private String name;


}
