package daggerok.github;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Getter
@Setter
@Validated
@JsonAutoDetect(
    fieldVisibility = NONE,
    setterVisibility = NONE,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    creatorVisibility = NONE
)
@ConfigurationProperties(prefix = "github")
public class GithubProperties {

  /**
   * github token, see:
   */
  @JsonProperty(access = READ_ONLY)
  @Pattern(regexp = "\\w+:\\w+")
  String token;
}
