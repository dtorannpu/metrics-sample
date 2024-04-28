rootProject.name = "metrics-sample"

pluginManagement {
    val jooqVersion: String by settings
    plugins {
        id("org.jooq.jooq-codegen-gradle") version jooqVersion
    }
}
