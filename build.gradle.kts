

plugins {
    application
    kotlin("jvm") version "1.4.32"
}

group = "rafikov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.9")
}

application {
    mainClass.set("kotlin/Main.kt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}