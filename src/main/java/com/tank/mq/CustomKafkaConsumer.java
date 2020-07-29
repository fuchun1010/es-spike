package com.tank.mq;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

/**
 * @author tank198435163.com
 */
@Slf4j
public class CustomKafkaConsumer {


  public CustomKafkaConsumer() {
    this.props = this.initProps();
    this.consumer = new KafkaConsumer<String, String>(props);
    this.consumer.subscribe(Collections.singletonList("demo"));
    this.executorService = this.initThreadPool();
  }

  public void acceptMessage() {
    if (Objects.isNull(thread)) {
      this.executorService.execute(() -> {
        val topic = this.props.getProperty("topic", "-");
        log.info("listening topic:[{}]", topic);
        while (running) {
          val consumerRecords = this.consumer.poll(Duration.ofMillis(100));

          for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            log.info("record value:[{}]", consumerRecord.value());
          }
        }
      });
    }
  }

  public ExecutorService initThreadPool() {
    val namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("kafka-pool-%d").build();
    return new ThreadPoolExecutor(1, 1,
            500L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1), namedThreadFactory,
            new ThreadPoolExecutor.AbortPolicy());
  }

  public void stopAcceptMessage() {
    this.running = false;
  }


  private Properties initProps() {
    Properties props = new Properties();
    val deSerial = "org.apache.kafka.common.serialization.StringDeserializer";
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, deSerial);
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, deSerial);
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-client-group");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
    props.put("topic", "demo");
    return props;
  }

  private final KafkaConsumer<String, String> consumer;

  private final ExecutorService executorService;

  private volatile Thread thread;

  private volatile boolean running = true;

  private final Properties props;
}
