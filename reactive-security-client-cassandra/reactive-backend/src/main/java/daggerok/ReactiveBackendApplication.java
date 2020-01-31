package daggerok;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.Collections;

import static java.util.Collections.singletonMap;

@EnableReactiveCassandraRepositories
@SpringBootApplication(exclude = CassandraDataAutoConfiguration.class)
public class ReactiveBackendApplication extends AbstractReactiveCassandraConfiguration {

  public static void main(String[] args) {

    new SpringApplicationBuilder(ReactiveBackendApplication.class)
        .properties(singletonMap("server.port", 8001))
        .run(args);
  }

  @Override
  protected String getKeyspaceName() {
    return "example";
  }

  @Override
  public SchemaAction getSchemaAction() {
    return SchemaAction.RECREATE;
  }
}
