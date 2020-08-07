package com.tank.mq.guess.assigin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author tank198435163.com
 */
public class SimpleConsumer {

  public SimpleConsumer() {
    this.props = this.initProps();
    this.consumer = this.initConsumer();
  }

  public Option<String> readAssignOffsetValue(@NonNull final String topic,
                                              @NonNull Integer partitionId,
                                              @NonNull Long offset) {

    this.consumer.subscribe(Collections.singletonList(topic));

    Set<TopicPartition> topicPartitions = Sets.newHashSet();
    while (topicPartitions.isEmpty()) {
      this.consumer.poll(Duration.ofMillis(100));
      topicPartitions = this.consumer.assignment();
    }

    val topicPartition = Stream.ofAll(topicPartitions)
            .filter(d -> d.topic().equals(topic))
            .filter(d -> partitionId.compareTo(d.partition()) == 0)
            .single();

    this.consumer.seek(topicPartition, offset);

    try {
      while (true) {
        val records = this.consumer.poll(Duration.ofMillis(100));
        val lists = records.records(topicPartition);
        for (ConsumerRecord<String, String> record : lists) {
          return Option.of(record.value());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      this.stop();
    }

    return Option.none();
  }

  public void consume() {
    val target = "demo";
    var tips = String.format("subscribe topic:[%s]", target);
    this.consumer.subscribe(Collections.singletonList(target));
    System.out.println(tips);
    Map<TopicPartition, OffsetAndMetadata> offsets = Maps.newHashMap();
    try {
      while (isRunning.get()) {
        final ConsumerRecords<String, String> records = this.consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          val topic = record.topic();
          val partition = record.partition();
          val offset = record.offset();
          val value = record.value();
          val key = Option.of(record.key()).getOrElse("-");
          tips = String.format("================================ topic:[%s], partition:[%d], offset:[%d],key:[%s], value:[%s]",
                  topic, partition, offset, key, value);
          System.out.println(tips);
          //prepare commit manually
          val topicPartition = new TopicPartition(topic, partition);
          val offsetAndMetadata = new OffsetAndMetadata(offset + 1);
          offsets.put(topicPartition, offsetAndMetadata);
        }
        //commit manually
        this.consumer.commitSync(offsets);
        offsets.clear();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      this.consumer.close();
    }
  }

  public void stop() {
    this.isRunning.set(false);
    this.consumer.wakeup();
  }

  private Properties initProps() {
    val props = new Properties();
    val deSerialClazz = StringDeserializer.class.getName();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deSerialClazz);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deSerialClazz);
    props.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple-consumer-1");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-consumer");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return props;
  }

  private KafkaConsumer<String, String> initConsumer() {
    return new KafkaConsumer<String, String>(this.props);
  }

  private final Properties props;

  private final KafkaConsumer<String, String> consumer;

  private final AtomicBoolean isRunning = new AtomicBoolean(true);
}
