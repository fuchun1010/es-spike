package com.tank.mq;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tank.dto.StandDto;
import com.tank.dto.WriteRecord;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Objects;
import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * @author tank198435163.com
 */
@Slf4j
public class CustomKafkaProducer {

  public CustomKafkaProducer() {
    this.props = this.initProps();
    this.producer = new KafkaProducer<String, String>(this.props);
    this.jsonMapper = new JsonMapper();
  }


  public <T extends StandDto> WriteRecord sendMessage(@NonNull final T data) {
    val topic = this.props.getProperty("topic");
    assert "demo".equalsIgnoreCase(topic);
    return Try.of(() -> this.jsonMapper.writeValueAsString(data))
            .map(json -> new ProducerRecord<String, String>(topic, json))
            .map(record -> {
              val future = this.producer.send(record, (metadata, exception) -> {
                if (Objects.nonNull(exception)) {
                  log.error("write to kafka topic :[{}] failure", topic);
                }
              });
              return Try.of(future::get).map(recordMetadata -> {
                log.info("topic :[{}], partition:[{}], offset:[{}]", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                return new WriteRecord(recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
              }).getOrElse(new WriteRecord());
            }).get();
  }

  private Properties initProps() {
    val props = new Properties();
    val serializer = "org.apache.kafka.common.serialization.StringSerializer";
    props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(KEY_SERIALIZER_CLASS_CONFIG, serializer);
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, serializer);
    props.put("topic", "demo");
    props.put(LINGER_MS_CONFIG, 10);
    return props;
  }

  private final Properties props;

  private KafkaProducer<String, String> producer;

  private JsonMapper jsonMapper;

}
