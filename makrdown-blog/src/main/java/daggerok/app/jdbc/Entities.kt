package daggerok.app.jdbc

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.annotation.Version
import java.time.LocalDateTime

data class Author(
    val email: String,
    val username: String,
    val firstName: String? = null,
    val lastName: String? = null,
    var version: Long? = 0,
    var updatedAt: LocalDateTime? = LocalDateTime.now(),
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @Id var id: Long? = null
)

data class Article(
    val title: String,
    val headline: String? = null,
    val content: String? = null,
    val authorId: Long? = null,
    var version: Long? = 0,
    var updatedAt: LocalDateTime? = LocalDateTime.now(),
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @Id var id: Long? = null
)

data class Post(
    var id: Long? = null,
    var title: String? = null,
    var updatedAt: LocalDateTime? = null,
    var createdAt: LocalDateTime? = null,
    var headline: String? = null,
    var content: String? = null,
    var username: String? = null
)
