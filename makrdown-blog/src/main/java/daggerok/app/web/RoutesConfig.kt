package daggerok.app.web

import daggerok.app.jdbc.Article
import daggerok.app.jdbc.ArticleRepository
import daggerok.app.jdbc.Post
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RenderingResponse
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers.elastic
import java.time.LocalDateTime
import java.util.*

@Configuration
class RoutesConfig(private val articleRepository: ArticleRepository,
                   private val markdownConverter: MarkdownConverter) {

  fun Post.render() = ArticleToBeRendered(
      id,
      title,
      markdownConverter.invoke(content),
      updatedAt,
      createdAt,
      markdownConverter.invoke(headline),
      username
  )

  data class ArticleToBeRendered(
      val articleId: Long? = null,
      val articleTitle: String? = null,
      val articleContent: String? = null,
      val articleUpdatedAt: LocalDateTime? = null,
      val articleCreatedAt: LocalDateTime? = null,
      val articleHeadline: String? = null,
      val authorUsername: String? = null
  )

  @Bean
  fun routes() = router {

    ("/").nest {

      contentType(MediaType.TEXT_HTML)

      GET("/") {
        //ok().render("index", mapOf("message" to "ololo trololo"))
        RenderingResponse.create("index")
            .modelAttribute("articles", articleRepository
                .findLatest20Articles()
                .toFlux()
                .map { it.render() }
                .subscribeOn(elastic()))
            .build()
            .cast(ServerResponse::class.java)
      }

      GET("/article/{id}") {
        val id = it.pathVariable("id").toLong()
        RenderingResponse.create("article")
            .modelAttribute("article", articleRepository
                .findPost(id)
                .orElseThrow { RuntimeException("article $id wan't found.") }
                .toMono()
                .map { it.render() }
                .subscribeOn(elastic()))
            .build()
            .cast(ServerResponse::class.java)
      }

      contentType(MediaType.APPLICATION_JSON_UTF8)

      GET("/api/{id}") {
        val id = it.pathVariable("id").toLong()
        ServerResponse.ok().body(
            articleRepository.findById(id)
                .toMono()
                .subscribeOn(elastic())
        )
      }

      GET("/api/**") {
        ServerResponse.ok().body(
            articleRepository.findAll()
                .toFlux()
                .subscribeOn(elastic())
        )
      }

      POST("/api/{id}") {
        val id = it.pathVariable("id").toLong()
        ServerResponse.ok().body(
            articleRepository.findById(id)
                .orElseThrow { RuntimeException("article $id wan't found.") }
                .toMono()
                .map { it.copy(content = """${it.content ?: ""} | updated""") }
                .map { articleRepository.save(it) }
                .subscribeOn(elastic())
        )
      }

      POST("/api/**") {
        val string = UUID.randomUUID().toString()
        ServerResponse.ok().body(
            articleRepository.save(Article(string, string, string))
                .toMono()
                .subscribeOn(elastic())
        )
      }
    }

    resources("/**", ClassPathResource("/public"))
  }
}
