package daggerok.app.jdbc

//import org.springframework.context.ApplicationListener
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
//import org.springframework.data.mapping.context.MappingContext
//import org.springframework.data.relational.core.conversion.BasicRelationalConverter
//import org.springframework.data.relational.core.mapping.RelationalMappingContext
//import org.springframework.data.relational.core.mapping.RelationalPersistentEntity
//import org.springframework.data.relational.core.mapping.RelationalPersistentProperty
//import org.springframework.data.relational.core.mapping.event.AfterSaveEvent
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
//import org.springframework.transaction.annotation.EnableTransactionManagement
//
//@Configuration
//@EnableJdbcRepositories
//@EnableTransactionManagement
//class JdbcConfig {
//
//  @Bean
//  fun dataSource() =
//      EmbeddedDatabaseBuilder()
//          .setType(EmbeddedDatabaseType.H2)
//          .build()
//
//  @Bean fun context() = RelationalMappingContext()
//
//  @Bean fun converter(context: MappingContext<out RelationalPersistentEntity<*>, out RelationalPersistentProperty>) = BasicRelationalConverter(context)
//
//  @Bean
//  fun afterSaveEventListener(): ApplicationListener<AfterSaveEvent> =
//      ApplicationListener { event ->
//        val entity = event.entity
//        println("saved entity: $entity")
//      }
//}

import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.data.jdbc.repository.config.JdbcConfiguration
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.lang.Thread.sleep
import java.sql.Clob
import java.sql.SQLException
import java.time.LocalDateTime
import javax.sql.DataSource

@Configuration
@EnableJdbcRepositories
@EnableTransactionManagement
class JdbcConfig : JdbcConfiguration() {

  @Bean
  fun dataSource(): DataSource = EmbeddedDatabaseBuilder()
      .setType(EmbeddedDatabaseType.H2)
      .build()

  operator fun Long?.plus(value: Long) = (this ?: 1) + value

  @Bean
  fun beforeSaveEventListener() = ApplicationListener<BeforeSaveEvent> { event ->
    val entity = event.entity
    sleep(1000)
    if (entity is Author) {
      entity.version += 1
      entity.updatedAt = LocalDateTime.now()
    }
    if (entity is Article) {
      entity.version += 1
      entity.updatedAt = LocalDateTime.now()
    }
    println("before save entity: $entity")
  }

  @Bean
  fun afterSaveEventListener() = ApplicationListener<AfterSaveEvent> { event ->
    val entity = event.entity
    println("after entity saved: $entity")
  }

  override fun jdbcCustomConversions() = JdbcCustomConversions(listOf(
      Converter<Clob, String> { clob ->
        try {
          if (clob.length() == 0L) ""
          else clob.getSubString(1, clob.length().toInt())
        }
        catch (e: SQLException) {
          throw IllegalStateException("Failed to convert CLOB to String: ${e.localizedMessage}", e)
        }
      }
  ))
}
