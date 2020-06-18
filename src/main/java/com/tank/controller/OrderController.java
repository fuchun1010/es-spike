package com.tank.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
