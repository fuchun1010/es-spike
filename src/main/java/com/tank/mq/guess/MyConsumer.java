package com.tank.mq.guess;

import lombok.NonNull;
import lombok.val;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * @author tank198435163.com
 */
public class MyConsumer<K, V> {

  public MyConsumer() {
    this.props = this.initProps();
  }

  public void handleReceivedMessage(@NonNull final String topic, @NonNull final Consumer<V> fun) {
    this.consumer.subscribe(Collections.singletonList(topic));
    do {
      val records = this.consumer.poll(Duration.ofMillis(100));
      for (ConsumerRecord<K, V> record : records) {
        fun.accept(record.value());
      }
    } while (true);
  }

  public MyConsumer<K, V> defaultJsonDeSerial() {
    val deSerialName = StringDeserializer.class.getName();
    val self = this.configDeSerial(deSerialName);
    this.consumer = new KafkaConsumer<K, V>(this.props);
    return self;
  }

  public MyConsumer<K, V> customDeSerial() {
    val protoStuffDeSerial = new ProtoStuffDeSerial<V>();
    val deSerialName = protoStuffDeSerial.getClass().getName();
    val self = this.configDeSerial(deSerialName);
    this.consumer = new KafkaConsumer<K, V>(this.props);
    return self;
  }

  private MyConsumer<K, V> configDeSerial(String deSerialName) {
    synchronized (this.props) {
      this.props.remove(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG);
      this.props.remove(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG);
      this.props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deSerialName);
      this.props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deSerialName);
    }
    return this;
  }


  private Properties initProps() {
    val props = new Properties();
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-consumer-1");
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return props;
  }

  private KafkaConsumer<K, V> consumer;

  private final Properties props;

}
