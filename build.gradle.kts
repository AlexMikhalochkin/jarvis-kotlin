import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("io.spring.dependency-management") version "1.1.6"
    id("jacoco")
    id("org.sonarqube") version "5.1.0.4882"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"
    id("com.diffplug.spotless") version "5.14.2"
    id("org.openapi.generator") version "5.1.1"
}

repositories {
    mavenCentral()
}

allprojects {
    group = "com.am"
    version = "0.0.1-SNAPSHOT"
}

java.sourceCompatibility = JavaVersion.VERSION_17

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("io.spring.dependency-management")
        plugin("jacoco")
        plugin("io.gitlab.arturbosch.detekt")
    }

    tasks.detekt.configure {
        reports {
            html {
                required.set(true)
                outputLocation.set(layout.buildDirectory.file("reports/detekt/report.html"))
            }
        }
    }

    detekt {
        toolVersion = "1.23.7"
        config.setFrom("$rootDir/configuration/detekt/detekt.yml")
        buildUponDefaultConfig = true
    }
}
