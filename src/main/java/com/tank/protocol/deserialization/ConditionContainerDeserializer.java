package com.tank.protocol.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.tank.protocol.condition.Condition;
import com.tank.protocol.condition.ConditionContainer;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author tank198435163.com
 */
public class ConditionContainerDeserializer extends StdDeserializer<ConditionContainer> {

  public ConditionContainerDeserializer() {
    this(null);
  }

  public ConditionContainerDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public ConditionContainer deserialize(@NonNull final JsonParser jsonParser,
                                        @NonNull final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    JsonNode treeNode = jsonParser.getCodec().readTree(jsonParser);
    ConditionContainer container = new ConditionContainer();
    return this.toConditionContainer(treeNode, container);
  }

  @SuppressWarnings("unchecked")
  private ConditionContainer toConditionContainer(@NonNull final JsonNode node, ConditionContainer container) {
    String logical = node.get("logical").asText();
    container.setLogical(logical);
    ArrayNode arrayNodes = ((ArrayNode) node.get("conditions"));
    for (JsonNode jsonNode : arrayNodes) {
      boolean hasChildren = Objects.nonNull(jsonNode.get("conditions")) && jsonNode.get("conditions").isArray();
      if (hasChildren) {
        ConditionContainer tmp = new ConditionContainer();
        String newLogical = node.get("logical").asText();
        tmp.setLogical(newLogical);
        //TODO 添加一个嵌入式的条件容器
        container.addCondition(tmp);
        this.toConditionContainer(jsonNode, tmp);
      } else {
        val op = jsonNode.get("op").asText();
        val fieldName = jsonNode.get("fieldName").asText();
        val type = jsonNode.get("type").asText();
        List<String> values = mapper.convertValue(jsonNode.get("values"), List.class);
        Condition<String> condition = new Condition<>();
        condition.setOp(op);
        condition.setFieldName(fieldName);
        condition.setType(type);
        condition.setValues(values);
        //add one condition
        container.addCondition(condition);
      }
    }

    return container;
  }


  private ObjectMapper mapper = new ObjectMapper();
  ;

}
