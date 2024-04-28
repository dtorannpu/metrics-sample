import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val jooqVersion: String by project

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.11.0")
    }
}

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.jooq.jooq-codegen-gradle")
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("org.flywaydb.flyway") version "10.11.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    jooqCodegen("org.postgresql:postgresql")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

kotlin.sourceSets.main {
    kotlin.srcDir(layout.buildDirectory.dir("generated-sources/jooq"))
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

//                // Generate the DAO classes
//                daos = true
//
//                // Annotate DAOs (and other types) with spring annotations, such as @Repository and @Autowired
//                // for auto-wiring the Configuration instance, e.g. from Spring Boot's jOOQ starter
//                springAnnotations = true
//
//                // Generate Spring-specific DAOs containing @Transactional annotations
//                springDao = true
            }
            target {

                // The destination package of your generated classes (within the
                // destination directory)
                //
                // jOOQ may append the schema name to this package if generating multiple schemas,
                // e.g. org.jooq.your.packagename.schema1
                // org.jooq.your.packagename.schema2
                packageName = "com.example.bookmanagement.db.jooq.gen"

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
}
