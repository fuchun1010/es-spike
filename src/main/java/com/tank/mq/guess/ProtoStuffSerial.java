package com.tank.mq.guess;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.val;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @author tank198435163.com
 */
public class ProtoStuffSerial<T> implements Serializer<T> {

  @Override
  public void configure(Map<String, ?> configs, boolean isKey) {
    String propertyName = isKey ? "key.serializer.encoding" : "value.serializer.encoding";
    Object encodingValue = configs.get(propertyName);
    if (encodingValue == null) {
      encodingValue = configs.get("serializer.encoding");
    }
    if (encodingValue instanceof String) {
      encoding = (String) encodingValue;
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public byte[] serialize(String topic, T data) {
    final Schema<T> schema = RuntimeSchema.getSchema((Class<T>) data.getClass());
    val buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    return ProtobufIOUtil.toByteArray(data, schema, buffer);
  }

  private String encoding = "UTF8";

}
