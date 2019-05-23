package daggerok.app.jdbc

import org.springframework.data.domain.Pageable
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import java.util.*

@Repository
interface AuthorRepository : CrudRepository<Author, Long>

@Repository
interface ArticleRepository : PagingAndSortingRepository<Article, Long> {

  @Query("""
    select a.title      title
         , a.headline   headline
         , a.content    content
         , a.author_id  authorId
         , a.version    version
         , a.created_at createdAt
         , a.updated_at updatedAt
         , a.id         id

    from article a

    where ( a.title     is not null and lower(a.title)     like concat('%', lower(:criteria), '%') )
       or ( a.headline  is not null and lower(a.headline)  like concat('%', lower(:criteria), '%') )
       or ( a.content   is not null and lower(a.content)   like concat('%', lower(:criteria), '%') )

    order by a.created_at desc
  """)
  fun fullTextSearch(@Param("criteria") criteria: String): List<Article>

  @Query("""
    select top 20

           ar.id
         , ar.title
         , ar.updated_at
         , ar.created_at
         , ar.headline
         , ar.content
         , au.username

    from article  ar
    join author   au
      on ar.author_id = au.id

    order by ar.created_at desc
  """)
  fun findLatest20Articles(): List<Post>

  @Query("""
    select top 1

           ar.id
         , ar.title
         , ar.updated_at
         , ar.created_at
         , ar.headline
         , ar.content
         , au.username

    from article  ar
    join author   au
      on ar.author_id = au.id

    where ar.id = :id
  """)
  fun findPost(@Param("id") id: Long): Optional<Post>
}
