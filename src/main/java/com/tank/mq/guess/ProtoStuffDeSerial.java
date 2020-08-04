package com.tank.mq.guess;

import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import lombok.val;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @param <T>
 * @author tank198435163.com
 */
public class ProtoStuffDeSerial<T> implements Deserializer<T> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    this.configs = configs;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T deserialize(String topic, byte[] data) {
    val isNotExists = configs.size() == 0 || !configs.containsKey("clazz");
    if (isNotExists) {
      throw new IllegalArgumentException("topic 参数必须指定");
    }
    final Class<T> clazz = ((Class<T>) configs.get("clazz"));
    val schema = RuntimeSchema.getSchema(clazz);
    val object = schema.newMessage();
    ProtobufIOUtil.mergeFrom(data, object, schema);
    return object;
  }

  private Map<String, ?> configs;
}
