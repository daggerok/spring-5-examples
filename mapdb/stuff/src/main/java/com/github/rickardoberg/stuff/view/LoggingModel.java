package com.github.rickardoberg.stuff.view;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import org.slf4j.LoggerFactory;

public class LoggingModel
    implements InteractionContextSink {
  ObjectMapper mapper;
  MappingJsonFactory jsonFactory = new MappingJsonFactory();

  public LoggingModel(ObjectMapper mapper) {
    this.mapper = mapper;
  }


  @Override
  public void apply(InteractionContext interactionContext) {
    try {
      StringWriter sw = new StringWriter();   // serialize
      JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(sw);
      mapper.writeValue(jsonGenerator, interactionContext);
      sw.close();

      LoggerFactory.getLogger(LoggingModel.class).info(sw.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
