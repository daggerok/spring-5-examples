package com.github.rickardoberg.cqrs.memory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rickardoberg.cqrs.event.InteractionContext;
import com.github.rickardoberg.cqrs.event.InteractionContextSink;
import com.github.rickardoberg.cqrs.event.store.EventSource;
import com.github.rickardoberg.cqrs.event.store.EventStore;
import org.slf4j.LoggerFactory;

public class FileEventStorage
    implements InteractionContextSink, Closeable {
  private ObjectMapper mapper;
  private MappingJsonFactory jsonFactory = new MappingJsonFactory();
  private Writer out;
  private EventSource eventSource;

  public FileEventStorage(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void apply(InteractionContext interactionContext) {
    try {
      JsonGenerator jsonGenerator = jsonFactory.createJsonGenerator(out);
      mapper.writeValue(jsonGenerator, interactionContext);
      out.append('\n');
      out.flush();
    } catch (IOException e) {
      LoggerFactory.getLogger(getClass()).warn("Failed to write event");
    }
  }

  public void load(File file, EventStore eventStore) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
      String line;
      while ((line = in.readLine()) != null) {
        InteractionContext context = deserialize(line);
        eventStore.add(context);
      }
    }
  }

  String serialize(InteractionContext interactionContext) throws JsonProcessingException {
    return mapper.writeValueAsString(interactionContext);
  }

  InteractionContext deserialize(String line) throws IOException {
    return mapper.readValue(line, InteractionContext.class);
  }

  public void save(File file, EventSource eventSource) throws FileNotFoundException, UnsupportedEncodingException {
    out = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");

    this.eventSource = eventSource;
    this.eventSource.addInteractionContextSink(this);
  }

  @Override
  public void close() throws IOException {
    eventSource.removeInteractionContextSink(this);
    out.close();
  }

}
