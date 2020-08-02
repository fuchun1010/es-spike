package com.tank.mq.guess;

import com.tank.dto.WriteRecord;
import io.vavr.CheckedFunction1;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @author tank198435163.com
 */
@Slf4j
public class MyProducer<K, V> {

  public MyProducer<K, V> defaultProducer() {
    this.producer = new KafkaProducer<K, V>(this.initProperties());
    return this;
  }

  public MyProducer<K, V> defaultProducer(@NonNull final String clientId) {
    this.clientId = clientId;
    this.producer = new KafkaProducer<K, V>(this.initProperties());
    return this;
  }

  public <S, D> MyProducer<K, V> customProducer(@NonNull final Class<S> serialClazz) {

    this.producer = new KafkaProducer<K, V>(this.initProperties(serialClazz));
    return this;
  }

  public <S, D> MyProducer<K, V> customProducer(@NonNull final String clientId, @NonNull final Class<S> serialClazz) {
    this.clientId = clientId;
    this.producer = new KafkaProducer<K, V>(this.initProperties(serialClazz));
    return this;
  }


  public <I> WriteRecord sendMessage(@NonNull final String topicName, @NonNull final I data, @NonNull final CheckedFunction1<I, V> serialFunction) {
    val record = new ProducerRecord<K, V>(topicName, Try.of(() -> serialFunction.apply(data)).get());
    return Try.of(() -> this.producer.send(record, (metadata, exception) -> {
      if (Objects.nonNull(exception)) {
        log.error("send topic:[{}] error", metadata.topic());
      }
    }).get(200, TimeUnit.MILLISECONDS)).map(recordMetadata -> {
      val writeRecord = new WriteRecord();
      val topic = recordMetadata.topic();
      val partitionId = recordMetadata.partition();
      val offset = recordMetadata.offset();
      return writeRecord.setOffset(offset).setPartitionId(partitionId).setTopicName(topic);
    }).getOrElse(new WriteRecord());
  }

  public <S, D> Properties initProperties(@NonNull final Class<S> serialClazz) {
    val props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serialClazz.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serialClazz.getName());
    props.put(ProducerConfig.CLIENT_ID_CONFIG, Option.of(this.clientId).getOrElse("kafka-producer"));
    props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
    props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
    props.put("clazz", serialClazz);
    return props;
  }

  public void closeProducer() {
    if (Objects.nonNull(this.producer)) {
      this.producer.close();
    }
  }

  private <S, D> Properties initProperties() {
    return this.initProperties(StringSerializer.class);
  }

  private KafkaProducer<K, V> producer;

  private String clientId;


}
