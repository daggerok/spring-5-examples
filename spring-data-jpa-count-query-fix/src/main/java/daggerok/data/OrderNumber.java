package daggerok.data;

import lombok.*;

import java.io.Serializable;
import java.util.Formattable;
import java.util.Formatter;

import static lombok.AccessLevel.PROTECTED;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(staticName = "of")
public class OrderNumber implements Serializable, Formattable {
  private static final long serialVersionUID = 868781104383062763L;

  @Getter
  @NonNull
  private Integer value;

  @Override
  public void formatTo(Formatter formatter, int flags, int width, int precision) {
    formatter.format("%d", value);
  }
}
