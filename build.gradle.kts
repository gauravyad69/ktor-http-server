import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
plugins {
    id ("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    id ("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"

    application

}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    }
    maven {
        url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }
}

dependencies {
    implementation("io.ktor", "ktor-client-core", "2.3.11")
    implementation("io.ktor", "ktor-client-cio", "2.3.11")
    implementation("io.ktor", "ktor-client-json", "2.3.11")
    implementation("io.ktor", "ktor-client-content-negotiation", "2.3.11")
    implementation("io.ktor", "ktor-client-logging", "2.3.11")
    implementation("io.ktor", "ktor-client-apache", "2.3.11")
    implementation("io.ktor", "ktor-client-serialization", "2.3.11")

    implementation("io.ktor", "ktor-jackson", "1.6.8")
    implementation("io.ktor", "ktor-server-status-pages", "2.3.11")
    implementation("io.ktor", "ktor-serialization-kotlinx-json", "2.3.11")
    implementation("io.ktor", "ktor-serialization-jackson", "2.3.11")
    implementation("io.ktor", "ktor-serialization", "2.3.11")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.3.0")

    implementation("io.ktor", "ktor-server-core", "2.3.11")
    implementation("io.ktor", "ktor-server-netty", "2.3.11")
    implementation("io.ktor", "ktor-server-content-negotiation", "2.3.11")




    implementation("io.ktor", "ktor-serialization-gson", "2.3.11")
    implementation("ch.qos.logback", "logback-classic", "1.4.12")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.10")
    testImplementation("io.ktor:ktor-server-tests:2.3.11")

}

tasks.test {
    useJUnitPlatform()
}


