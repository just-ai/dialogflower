plugins {
    application
    kotlin("jvm") version "1.3.71"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "com.justai.dialogflower"
version = "1.0.0"

val jaicfVersion = "0.9.0"

application {
    mainClassName = "com.justai.dialogflower.ServerKt"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-server-netty:1.3.1")

    implementation("com.justai.jaicf:core:$jaicfVersion")
    implementation("com.justai.jaicf:dialogflow:$jaicfVersion")
    implementation("com.justai.jaicf:yandex-alice:$jaicfVersion")
}

tasks.withType<Jar> {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to application.mainClassName
            )
        )
    }
}

tasks.create("stage") {
    dependsOn("shadowJar")
}