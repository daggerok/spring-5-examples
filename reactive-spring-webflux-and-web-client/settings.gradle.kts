rootProject.name = Globals.Project.artifactId
pluginManagement {
  repositories {
    gradlePluginPortal()
    //maven { url = uri("https://repo.spring.io/snapshot/") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
    maven { url = uri("https://repo.spring.io/milestone/") }
    mavenCentral()
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id == "org.springframework.boot") {
        useModule("org.springframework.boot:spring-boot-gradle-plugin:${requested.version}")
      }
    }
  }
}
