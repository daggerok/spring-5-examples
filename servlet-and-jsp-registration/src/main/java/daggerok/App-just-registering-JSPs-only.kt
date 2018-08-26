//package daggerok
//
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.boot.builder.SpringApplicationBuilder
//import org.springframework.boot.runApplication
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.ControllerAdvice
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.ModelAttribute
//
//// Just registering JSPs only
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
//@SpringBootApplication
//class App : SpringBootServletInitializer() {
//
//  override fun configure(builder: SpringApplicationBuilder?): SpringApplicationBuilder {
//    return builder?.sources(App::class.java)
//        ?: throw RuntimeException("This is shouldn't happen, nut it's kotlin, baby! :)")
//  }
//}
//
//fun main(args: Array<String>) {
//  runApplication<App>(*args)
//}
