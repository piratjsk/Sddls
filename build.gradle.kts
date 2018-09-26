import org.gradle.language.jvm.tasks.ProcessResources
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.jvm.tasks.Jar

plugins {
    java
    kotlin("jvm") version "1.2.71"
}

group = project.property("group")!!
version = project.property("version")!!

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://dl.bintray.com/spekframework/spek-dev")
}

dependencies {
    compile("org.bukkit:bukkit:${project.property("bukkitVersion")}")

    testImplementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:2.0.0-alpha.1") {
        exclude("org.jetbrains.kotlin")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.1.0")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:2.0.0-alpha.1") {
        exclude("org.junit.platform")
        exclude("org.jetbrains.kotlin")
    }
    testRuntimeOnly(kotlin("reflect"))
    testImplementation("org.mockito:mockito-inline:+")
}

val test : Test by tasks
test.useJUnitPlatform { includeEngines("spek") }
        "group" to project.property("group"),
        "name" to project.property("name"),
        "version" to project.property("version")
)
processResources.apply {
    from("src/main/resources") {
        filter<ReplaceTokens>("tokens" to tokens)
    }
}

val jar = tasks.getByName("jar") as Jar
jar.apply { from("LICENSE") }
