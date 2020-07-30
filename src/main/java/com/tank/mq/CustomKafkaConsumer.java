package com.tank.mq;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

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

    this.executorService = this.initThreadPool();
  }

  public void fetchMessageFrom(@NonNull final String topic,
                               @NonNull final Integer partitionId,
                               @NonNull final Long offset) {
    Preconditions.checkArgument(topic.trim().length() > 0, "topic name not allowed empty string");
    Preconditions.checkArgument(partitionId >= 0, "partition id is not allowed less than zero");
    Preconditions.checkArgument(offset >= 0, "offset is not allowed less than zero");

    val targetTopic = new TopicPartition(topic, partitionId);
    this.consumer.assign(Collections.singletonList(targetTopic));

    this.consumer.seekToBeginning(Collections.singletonList(targetTopic));

    do {
      val records = this.consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<String, String> record : records) {
        if (record.offset() == offset) {
          System.out.println("topic = " + topic + ", partitionId = " + partitionId + ", value = " + record.value());
          break;
        }
      }
    } while (true);

  }

  public void acceptMessage() {
    this.consumer.subscribe(Collections.singletonList("demo"));
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
