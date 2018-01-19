package daggerok.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor(staticName = "of")
public class User implements Serializable {

  private static final long serialVersionUID = -8234636533428149414L;

  UUID uuid;
  String name;
}
