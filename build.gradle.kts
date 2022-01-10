import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun allPlugins(p: Project) {
  p.apply(plugin="java")
  p.apply(plugin="eclipse")
}

fun allRepos(r: RepositoryHandler) {
  r.mavenLocal()
  r.mavenCentral()
}

plugins {
  println("plugins is ")
  println(this)
  //allPlugins(this);
  eclipse
  java
}

dependencies {
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

fun dependsOnIPipe(dhs: DependencyHandlerScope) {
   dhs.implementation(project("i_pipe.adligo.org"))
}

fun dependsOnITests4j(dhs: DependencyHandlerScope) {
   dhs.implementation(project("i_tests4j.adligo.org"))
}

fun dependsOnMockito(dhs: DependencyHandlerScope) {
   dhs.implementation("org.mockito:mockito-all:1.10.19")
}

fun dependsOnPipe(dhs: DependencyHandlerScope) {
   dependsOnIPipe(dhs)
   dhs.implementation(project("pipe.adligo.org"))
}

fun dependsOnTests4j(dhs: DependencyHandlerScope) {
  dependsOnITests4j(dhs)
  dhs.implementation(project("tests4j.adligo.org"))
}

project(":i_pipe.adligo.org") {
  allPlugins(this)
  repositories {
    allRepos(this)
  }
}

project(":i_tests4j.adligo.org") {
  allPlugins(this)
  repositories {
    allRepos(this)
  }
}

project(":mockito_ext.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnMockito(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":pipe.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnIPipe(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":pipe_tests.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnPipe(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":tests4j.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnITests4j(this)
  }
  repositories {
    allRepos(this)
  }
}

project(":tests4j_4mockito.adligo.org") {
  allPlugins(this)
  dependencies {
    dependsOnTests4j(this)
  }
  repositories {
    allRepos(this)
  }
}
repositories {
  mavenLocal()
  mavenCentral()
}

/**
I have found that the JAVA_HOME environment variable that is set when your run this task ;
    gradle cleanEclipse eclipse
is the one that is included in the Eclipse BuildPath
*/
tasks.register<GradleBuild>("ecp") {
    tasks = listOf("cleanEclipseClasspath", "eclipseClasspath")
}




