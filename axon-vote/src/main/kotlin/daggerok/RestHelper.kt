package daggerok

import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest

object RestHelper {

  fun builder(request: HttpServletRequest): UriComponentsBuilder {
    val uri = UriComponentsBuilder.fromHttpUrl(request.requestURL.toString()).build()
    return UriComponentsBuilder.fromHttpUrl(java.lang.String.format("%s://%s:%d", uri.scheme, uri.host, uri.port))
  }
}
