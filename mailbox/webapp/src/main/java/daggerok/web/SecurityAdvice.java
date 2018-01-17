package daggerok.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME;

@Slf4j
@ControllerAdvice
public class SecurityAdvice {

  @ModelAttribute("_csrf")
  Mono<CsrfToken> csrfToken(final ServerWebExchange exchange) {

    final Mono<CsrfToken> csrfToken = exchange.getAttribute(CsrfToken.class.getName());

    return csrfToken.doOnSuccess(token -> exchange.getAttributes()
                                                  .put(DEFAULT_CSRF_ATTR_NAME, token));
  }

  @ModelAttribute("currentUser")
  Mono<Principal> currentUser(final Mono<Principal> currentUser) {
    return currentUser;
  }
}
