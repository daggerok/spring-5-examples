package daggerok.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MovieEvent implements Serializable {

  private static final long serialVersionUID = -1557020829109982804L;

  Movie movie;
  LocalDateTime updatedAt;
}
