package com.github.rickardoberg.cqrs.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InteractionContext {
  private String type;
  private long version;
  private Date timestamp;
  private Map<String, String> attributes = new HashMap<>();
  private Interaction interaction;

  public InteractionContext(String type, long version, Date timestamp, Map<String, String> attributes, Interaction interaction) {
    this.type = type;
    this.version = version;
    this.timestamp = timestamp;
    this.attributes = attributes;
    this.interaction = interaction;
  }

  public String getType() {
    return type;
  }

  public long getVersion() {
    return version;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public Map<String, String> getAttributes() {
    return attributes;
  }

  public Interaction getInteraction() {
    return interaction;
  }
}
