//package daggerok
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import lombok.SneakyThrows
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.builder.SpringApplicationBuilder
//import org.springframework.boot.runApplication
//import org.springframework.boot.web.servlet.ServletRegistrationBean
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
//import org.springframework.context.annotation.Bean
//import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.ModelAttribute
//import javax.servlet.http.HttpServlet
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//// Registering both: Servlets and JSPs
//
//@ControllerAdvice
//class MessageAdvice {
//
//  @ModelAttribute("message")
//  fun message(@Value("\${application.message:Hello JSP}") message: String) = message
//}
//
//@Controller
//class JspIndexPage {
//
//  @GetMapping(*["/jsp", "/jsp/**"]) fun index() = "index"
//}
//
//class ApplicationServlet(private val mapper: ObjectMapper) : HttpServlet() {
//
//  @SneakyThrows
//  override fun service(request: HttpServletRequest, response: HttpServletResponse) {
//    val out = response.writer
//    response.contentType = APPLICATION_JSON_UTF8_VALUE
//    out.use {
//      out.println(
//          mapper
//              .writerWithDefaultPrettyPrinter()
//              .writeValueAsString(mapOf("ololo" to "trololo"))
//      )
//    }
//  }
//}
//
//@SpringBootApplication
//class App : SpringBootServletInitializer() {
//
//  override fun configure(builder: SpringApplicationBuilder?): SpringApplicationBuilder {
//    return builder?.sources(App::class.java)
//        ?: throw RuntimeException("This is shouldn't happen, nut it's kotlin, baby! :)")
//  }
//
//  @Bean
//  fun mapper() = ObjectMapper()
//
//  @Bean
//  fun applicationServlet(mapper: ObjectMapper) = ApplicationServlet(mapper)
//
//  @Bean
//  fun exampleServletBean(applicationServlet: ApplicationServlet): ServletRegistrationBean<*> {
//    val paths = arrayOf("/привед/*", "/превед!/*", "/hola/*", "/hello/*", "/app/*")
//    val bean = ServletRegistrationBean(applicationServlet, *paths)
//    bean.setLoadOnStartup(1)
//    return bean
//  }
//}
//
//fun main(args: Array<String>) {
//  runApplication<App>(*args)
//}
