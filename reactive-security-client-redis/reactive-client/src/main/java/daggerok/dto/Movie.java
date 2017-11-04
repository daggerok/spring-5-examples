package daggerok.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Movie implements Serializable {

  private static final long serialVersionUID = 8016029622462112631L;

  String id;
  String title;
}
