import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.gradle.language.jvm.tasks.ProcessResources
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.jvm.tasks.Jar

plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
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
    implementation("org.bukkit:bukkit:1.13.1-R0.1-SNAPSHOT")

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

bukkit {
    main = "net.piratjsk.sddls.Sddls"
    apiVersion = "1.13"

    commands {
        create("sddls") {
            description = "Prints information about plugin. With 'reload' argument reloads plugin configuration."
        }
    }
    permissions {
        create("sddls.reload") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows you to use 'sddls reload' command."
        }
        create("sddls.bypass") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows you to bypass signed saddles protection."
        }
    }
}

val jar : Jar by tasks
jar.from("LICENSE")
