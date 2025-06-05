import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

// plugin versioning
val VERSION = "1.9.0" // kotlin version actualizada
val JVM = 17          // actualizado a 17 para soporte moderno

// base of output jar name, full jar will be "Argi-Kotlin-{MINECRAFT_VERSION}.jar"
val OUTPUT_JAR_NAME = "Argi-Kotlin"

// target will be set por la tarea
var target = ""

// output jar text que indica kotlin-reflect incluido
var useReflect = ""

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JVM))
    }
}

configurations {
    create("resolvableImplementation") {
        isCanBeResolved = true
        isCanBeConsumed = true
    }
}

dependencies {
    compileOnly(platform("org.jetbrains.kotlin:kotlin-bom"))
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    configurations["resolvableImplementation"]("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    if (project.hasProperty("1.12")) {
        compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
        target = "1.12"
    } else if (project.hasProperty("1.16")) {
        compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
        target = "1.16"
    } else if (project.hasProperty("1.17")) {
        compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
        target = "1.17"
    } else if (project.hasProperty("1.18")) {
        compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
        target = "1.18"
    } else if (project.hasProperty("1.19")) {
        compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
        target = "1.19"
    } else if (project.hasProperty("1.20")) {
        compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
        target = "1.20"
    } else if (project.hasProperty("1.21")) {
        compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
        target = "1.21"
    }

    if (project.hasProperty("reflect")) {
        configurations["resolvableImplementation"]("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
        useReflect = "-reflect"
    }

    sourceSets["main"].resources.srcDir("src/mc-${target}/resources")
    sourceSets["main"].java.srcDir("src/mc-${target}")
}

tasks {
    named<ShadowJar>("shadowJar") {
        doFirst {
            val supportedMinecraftVersions = setOf("1.12", "1.16", "1.17", "1.18", "1.19", "1.20", "1.21")
            if (!supportedMinecraftVersions.contains(target)) {
                throw Exception("Invalid Minecraft version! Supported versions are: 1.12, 1.16, 1.17, 1.18, 1.19, 1.20, 1.21")
            }
        }

        classifier = ""
        configurations = mutableListOf(project.configurations.named("resolvableImplementation").get())
    }

    build {
        dependsOn(shadowJar)
    }

    test {
        testLogging.showStandardStreams = true
    }
}

gradle.taskGraph.whenReady {
    tasks.named<ShadowJar>("shadowJar") {
        baseName = "${OUTPUT_JAR_NAME}${useReflect}-${VERSION}-jvm${JVM}-mc${target}"
    }
}
