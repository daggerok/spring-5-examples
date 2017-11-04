package daggerok.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Json {

  final ObjectMapper objectMapper;

  public <T extends Object> String stringify(final T object) {
    return Try.of(() -> objectMapper.writeValueAsString(object))
              .getOrElse(() -> null);
  }

  public <T extends Object> T parse(final String json, Class<T> type) {
    return Try.of(() -> objectMapper.readValue(json, type))
              .getOrElse(() -> null);
  }
}
