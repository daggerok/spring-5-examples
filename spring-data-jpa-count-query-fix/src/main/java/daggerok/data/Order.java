package daggerok.data;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Formattable;
import java.util.Formatter;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = PROTECTED)
@RequiredArgsConstructor(staticName = "of")
public class Order implements Serializable, Formattable {
  private static final long serialVersionUID = -3959467201341615143L;

  @Id
  @Setter(NONE)
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @NonNull
  private OrderNumber orderNumber;

  @NonNull
  @Column(precision = 5, scale = 2)
  private Price price;

  @Override
  public void formatTo(Formatter formatter, int flags, int width, int precision) {
    formatter.format("%s", price);
  }
}
