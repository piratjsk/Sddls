import org.gradle.language.jvm.tasks.ProcessResources
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.jvm.tasks.Jar

plugins {
    java
}

group = project.property("group")!!
version = project.property("version")!!

repositories {
    jcenter()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compile("org.bukkit:bukkit:${project.property("bukkitVersion")}")
}

val processResources = tasks.getByName("processResources") as ProcessResources
val tokens = mapOf(
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
