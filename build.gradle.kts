import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

kotlin {
    jvmToolchain(21)
}

sourceSets {
    val integrationTest by creating {
        kotlin.srcDir("src/integrationTest/kotlin")
        resources.srcDir("src/integrationTest/resources")
        compileClasspath += sourceSets["main"].output
        runtimeClasspath += output + compileClasspath
    }
}

configurations {
    val integrationTestImplementation by getting {
        extendsFrom(configurations.testImplementation.get())
    }
    val integrationTestRuntimeOnly by getting {
        extendsFrom(configurations.testRuntimeOnly.get())
    }
}

dependencies {
    testImplementation(kotlin("test"))

    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        testFramework(TestFrameworkType.Starter)
    }
}

intellijPlatform {
    instrumentCode = false

    pluginConfiguration {
        name = "changelists-ui-test"
        version = project.version.toString()
    }
}

tasks {
    val integrationTest by registering(Test::class) {
        description = "Runs IDE Starter integration tests."
        group = "verification"
        useJUnitPlatform()

        testClassesDirs = sourceSets["integrationTest"].output.classesDirs
        classpath = sourceSets["integrationTest"].runtimeClasspath

        shouldRunAfter(test)
    }

    check {
        dependsOn(integrationTest)
    }

    wrapper {
        gradleVersion = "9.4.1"
    }
}