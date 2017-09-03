package daggerok.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.dsl.Http;
import reactor.core.publisher.Flux;

@Configuration
public class ServerSideEventsConfig {

  @Bean
  public IntegrationFlow sseFlow() {

    return IntegrationFlows.from(Http.inboundReactiveGateway("/sse")
                                     .requestMapping(m -> m.produces(MediaType.TEXT_EVENT_STREAM_VALUE)))
                           .handle((p, h) -> Flux.just("foo", "bar", "baz"))
                           .get();
  }
/*
  @Bean
  public IntegrationFlow uppercase(MongoOperations mongoOperations) {
    return IntegrationFlows.from(Function.class)
                           .handle(MongoDb.outboundGateway(mongoOperations)
                                          .queryFunction(msg ->
                                                             Query.query(Criteria.where("name")
                                                                                 .is(msg.getPayload())))
                                          .collectionNameExpression("headers.collection")
                                          .expectSingleResult(true)
                                          .entityClass(Person.class))
                           .get();
  }
*/
}
