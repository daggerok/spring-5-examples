package daggerok.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Movie implements Serializable {

  private static final long serialVersionUID = -4371272260347529772L;

  @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1) String id;
  @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 2) String data;
  @NonNull String title;
}
