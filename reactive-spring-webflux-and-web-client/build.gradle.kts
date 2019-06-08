import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  id("org.springframework.boot") version Globals.Gradle.Plugin.springBootVersion
  id("io.franzbecker.gradle-lombok") version Globals.Gradle.Plugin.lombokVersion
  id("io.spring.dependency-management") version Globals.Gradle.Plugin.dependencyManagementVersion
  id("com.github.ben-manes.versions") version Globals.Gradle.Plugin.versionsVersion
  // gradle dependencyUpdates -Drevision=release
}

group = Globals.Project.groupId
version = Globals.Project.version

extra["junit.version"] = Globals.junitVersion
extra["lombok.version"] = Globals.lombokVersion
extra["junit-jupiter.version"] = Globals.junitJupiterVersion

java {
  sourceCompatibility = Globals.javaVersion
  targetCompatibility = Globals.javaVersion
}

repositories {
  mavenCentral()
  //maven { url = uri("https://repo.spring.io/snapshot/") }
  maven { url = uri("https://repo.spring.io/milestone/") }
}

lombok {
  version = Globals.lombokVersion
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("io.projectreactor:reactor-test")

  implementation("io.vavr:vavr:${Globals.vavrVersion}")
  annotationProcessor("org.projectlombok:lombok")

  testImplementation(platform("org.junit:junit-bom:${Globals.junitJupiterVersion}"))
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("junit:junit")
}

tasks {
  withType(Wrapper::class.java) {
    gradleVersion = Globals.Gradle.wrapperVersion
    distributionType = Wrapper.DistributionType.BIN
  }

  withType<Test> {
    useJUnitPlatform()
    testLogging {
      showExceptions = true
      showStandardStreams = true
      events(PASSED, SKIPPED, FAILED)
    }
  }

  create<Zip>("sources") {
    dependsOn("clean")
    shouldRunAfter("clean", "assemble")
    description = "Archives sources in a zip file"
    group = "Archive"
    from("buildSrc") {
      into("buildSrc")
    }
    from("src") {
      into("src")
    }
    from(".gitignore")
    from(".java-version")
    from(".travis.yml")
    from("build.gradle.kts")
    from("pom.xml")
    from("README.md")
    from("settings.gradle.kts")
    archiveFileName.set("${project.buildDir}/sources-${project.version}.zip")
  }

  named("clean") {
    doLast {
      delete(
          project.buildDir,
          "${project.projectDir}/out"
      )
    }
  }
}

defaultTasks("clean", "sources", "build")
