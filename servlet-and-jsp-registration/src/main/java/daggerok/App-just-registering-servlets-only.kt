//package daggerok
//
//import com.fasterxml.jackson.databind.ObjectMapper
//import lombok.Cleanup
//import lombok.SneakyThrows
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.runApplication
//import org.springframework.boot.web.servlet.ServletRegistrationBean
//import org.springframework.context.annotation.Bean
//import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
//import javax.servlet.http.HttpServlet
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
//// Just registering Servlets only
//
//class ApplicationServlet(private val mapper: ObjectMapper) : HttpServlet() {
//
//  @SneakyThrows
//  override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
//    @Cleanup val out = response.writer
//    response.contentType = APPLICATION_JSON_UTF8_VALUE
//    out.println(mapper
//        .writerWithDefaultPrettyPrinter()
//        .writeValueAsString(mapOf("ololo" to "trololo")))
//  }
//}
//
//@SpringBootApplication
//class App {
//
//  @Bean fun mapper() = ObjectMapper()
//
//  @Bean fun applicationServlet(mapper: ObjectMapper) = ApplicationServlet(mapper)
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
