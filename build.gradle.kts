import org.flywaydb.gradle.task.AbstractFlywayTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.codegen.gradle.CodegenTask

buildscript {
    dependencies {
        classpath(libs.org.flywaydb.flyway.database.postgresql)
    }
}

plugins {
    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.io.spring.dependency.management)
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.spring)
    alias(libs.plugins.org.jooq.jooq.codegen.gradle)
    alias(libs.plugins.org.jlleitschuh.gradle.ktlint)
    alias(libs.plugins.co.uzzu.dotenv.gradle)
    alias(libs.plugins.org.flywaydb.flyway)
    alias(libs.plugins.org.openapi.generator)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.org.springframework.boot.spring.boot.starter.actuator)
    implementation(libs.org.springframework.boot.spring.boot.starter.jooq)
    implementation(libs.org.jooq.jooq)
    implementation(libs.org.springframework.boot.spring.boot.starter.web)
    implementation(libs.com.fasterxml.jackson.module.jackson.module.kotlin)
    implementation(libs.org.flywaydb.flyway.core)
    implementation(libs.org.flywaydb.flyway.database.postgresql)
    implementation(libs.org.jetbrains.kotlin.kotlin.reflect)
    implementation(libs.org.springframework.boot.spring.boot.starter.validation)
    implementation(libs.org.springdoc.springdoc.openapi.starter.webmvc.ui)
    testImplementation(libs.org.testcontainers.junit.jupiter)
    testImplementation(libs.org.springframework.boot.spring.boot.testcontainers)
    testImplementation(libs.org.testcontainers.postgresql)
    developmentOnly(libs.org.springframework.boot.spring.boot.devtools)
    developmentOnly(libs.org.springframework.boot.spring.boot.docker.compose)
    runtimeOnly(libs.io.micrometer.micrometer.registry.prometheus)
    runtimeOnly(libs.org.postgresql.postgresql)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    jooqCodegen(libs.org.postgresql.postgresql)
    testImplementation(libs.io.kotest.kotest.runner.junit5)
    testImplementation(libs.io.kotest.kotest.assertions.core)
    testImplementation(libs.io.kotest.extensions.kotest.extensions.testcontainers)
    testImplementation(libs.io.kotest.extensions.kotest.extensions.spring)
    implementation(libs.io.micrometer.micrometer.tracing.bridge.otel)
    implementation(libs.io.opentelemetry.opentelemetry.exporter.otlp)
    implementation(libs.net.ttddyy.observation.datasource.micrometer.spring.boot)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

kotlin.sourceSets.main {
    kotlin.srcDir(layout.buildDirectory.dir("generated-sources/jooq"))
    kotlin.srcDir(layout.buildDirectory.dir("generated-sources/api/src/main/kotlin"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jooq {
    configuration {

        // Configure the database connection here
        jdbc {
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:${env.fetch("POSTGRES_PORT")}/${env.fetch("POSTGRES_DB")}"
            user = env.fetch("POSTGRES_USER")
            password = env.fetch("POSTGRES_PASSWORD")
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            strategy {
                name = "org.jooq.codegen.example.JPrefixGeneratorStrategy"
            }
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"

                // All elements that are generated from your schema (A Java regular expression.
                // Use the pipe to separate several expressions) Watch out for
                // case-sensitivity. Depending on your database, this might be
                // important!
                //
                // You can create case-insensitive regular expressions using this syntax: (?i:expr)
                //
                // Whitespace is ignored and comments are possible.
                includes = ".*"

                // All elements that are excluded from your schema (A Java regular expression.
                // Use the pipe to separate several expressions). Excludes match before
                // includes, i.e. excludes have a higher priority
                excludes = """
                flyway_schema_history
                """

                // The schema that is used locally as a source for meta information.
                // This could be your development schema or the production schema, etc
                // This cannot be combined with the schemata element.
                //
                // If left empty, jOOQ will generate all available schemata. See the
                // manual"s next section to learn how to generate several schemata
                inputSchema = "public"
            }

            // Generation flags: See advanced configuration properties
            generate {
                // Tell the KotlinGenerator to generate properties in addition to methods for these paths. Default is true.
                isImplicitJoinPathsAsKotlinProperties = true

                // Workaround for Kotlin generating setX() setters instead of setIsX() in byte code for mutable properties called
                // <code>isX</code>. Default is true.
                isKotlinSetterJvmNameAnnotationsOnIsPrefix = true

                // Generate POJOs as data classes, when using the KotlinGenerator. Default is true.
                isPojosAsKotlinDataClasses = true

                // Generate non-nullable types on POJO attributes, where column is not null. Default is false.
                isKotlinNotNullPojoAttributes = true

                // Generate non-nullable types on Record attributes, where column is not null. Default is false.
                isKotlinNotNullRecordAttributes = true

                // Generate non-nullable types on interface attributes, where column is not null. Default is false.
                isKotlinNotNullInterfaceAttributes = true

                // Generate defaulted nullable POJO attributes. Default is true.
                isKotlinDefaultedNullablePojoAttributes = false

                // Generate defaulted nullable Record attributes. Default is true.
                isKotlinDefaultedNullableRecordAttributes = false

//                isDaos = true
//                isSpringAnnotations = true
//                isSpringDao = true
            }
            target {

                // The destination package of your generated classes (within the
                // destination directory)
                //
                // jOOQ may append the schema name to this package if generating multiple schemas,
                // e.g. org.jooq.your.packagename.schema1
                // org.jooq.your.packagename.schema2
                packageName = "com.example.metricssample.db.jooq.gen"

                // The destination directory of your generated classes
                // directory = "src/main/kotlin"
            }
        }
    }
}

flyway {
    url = "jdbc:postgresql://localhost:${env.fetch("POSTGRES_PORT")}/${env.fetch("POSTGRES_DB")}"
    user = env.fetch("POSTGRES_USER")
    password = env.fetch("POSTGRES_PASSWORD")
}

ktlint {
    filter {
        exclude { element ->
            element.file.path.contains("generated")
        }
        include("**/kotlin/**")
    }
    version.set("1.5.0")
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/openapi.yaml")
    outputDir.set(
        layout.buildDirectory
            .dir("generated-sources/api")
            .get()
            .toString(),
    )
    apiPackage.set("com.example.metricssample.api")
    invokerPackage.set("com.example.metricssample.api.invoker")
    modelPackage.set("com.example.metricssample.api.model")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "useSpringBoot3" to "true",
            "interfaceOnly" to "true",
        ),
    )
}

tasks {
    withType<AbstractFlywayTask> {
        notCompatibleWithConfigurationCache("because https://github.com/flyway/flyway/issues/3550")
    }
    withType<CodegenTask> {
        notCompatibleWithConfigurationCache("because https://github.com/jOOQ/jOOQ/issues/16997")
    }
}
