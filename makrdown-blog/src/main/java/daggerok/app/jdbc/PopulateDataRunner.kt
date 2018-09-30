package daggerok.app.jdbc

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.lang.Thread.sleep

@Component
class PopulateDataRunner(val articleRepository: ArticleRepository,
                         val authorRepository: AuthorRepository) : ApplicationRunner {

  @Transactional
  override fun run(args: ApplicationArguments?) {
    articleRepository.deleteAll()
    authorRepository.deleteAll()

    val author = authorRepository.save(Author("daggerok@gmail.com", "maksimko"))

    listOf(
        Article(
            ""
        )
    )

    listOf("ololo", "trololo")
        .map {
          Article(
              title = it,
              content = "# content $it",
              headline = "**headline $it**",
              authorId = author.id
          )
        }
        .forEach { articleRepository.save(it) }
  }
}
