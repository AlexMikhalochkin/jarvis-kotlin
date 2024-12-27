val kotlinLoggingVersion = "2.1.21"

plugins {
    id("OpenApiGeneratorPlugin")
}

tasks.getByName("bootJar") {
    enabled = false
}

openApiGenerate {
    inputSpec.set("$rootDir/configuration/codegenerator/smart-things.yaml")
}

dependencies {
    implementation(project(":momomo-core"))
    implementation(project(":momomo-notifier-smart-things"))

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("javax.validation:validation-api:$validationApiVersion")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
