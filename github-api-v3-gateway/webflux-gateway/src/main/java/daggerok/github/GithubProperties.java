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

// we have to use @JsonAutoDetect + @JsonProperty(access = READ_ONLY)
// if we wanna show only specifiyed serialization, otherwise, spring will
// generate proxy with additional wrapped fields and you will see this:
/*
{
    "advisors": [
        {
            "advice": {},
            "order": 2147483647,
            "perInstance": true,
            "pointcut": {
                "classFilter": {},
                "methodMatcher": {
                    "runtime": false
                }
            }
        }
    ],
    "proxiedInterfaces": [],
    "targetClass": "daggerok.github.GithubProperties",
    "targetSource": {
        "static": true,
        "target": {
            "token": "my:token"
        },
        "targetClass": "daggerok.github.GithubProperties"
    },
    "token": "my:token"
}
*/
@JsonAutoDetect(
    fieldVisibility = NONE,
    setterVisibility = NONE,
    getterVisibility = NONE,
    isGetterVisibility = NONE,
    creatorVisibility = NONE
)
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "github")
public class GithubProperties {

  /**
   * github token in format username:password
   */
  @JsonProperty(access = READ_ONLY)
  @Pattern(regexp = "\\w+:\\w+")
  String token;
}
