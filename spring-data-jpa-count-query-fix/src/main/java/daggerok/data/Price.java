package daggerok.data;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Formattable;
import java.util.Formatter;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static lombok.AccessLevel.PROTECTED;

@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(staticName = "of")
public class Price implements Serializable, Formattable {
  private static final long serialVersionUID = 17950070619021432L;

  @NonNull
  private BigDecimal value;

  @Column(precision = 5, scale = 2)
  public BigDecimal getValue() {
    return scale(value);
  }

  public BigDecimal setValue(final BigDecimal value) {
    return this.value = scale(value);
  }

  private static final BigDecimal scale(final BigDecimal price) {
    return price.setScale(2, ROUND_HALF_UP);
  }

  @Override
  public void formatTo(Formatter formatter, int flags, int width, int precision) {
    formatter.format("%s", value);
  }
}
