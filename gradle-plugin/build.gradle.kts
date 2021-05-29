plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.4.31"
}

gradlePlugin {
    plugins {
        create(name) {
            id = "$group.klip"
            implementationClass = "$id.KlipPlugin"
        }
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(kotlin("gradle-plugin", "_"))
    implementation("dev.petuska:klip-core")
    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:_")

    testImplementation(kotlin("test-junit"))
}

tasks {
    processResources {
        doLast {
            destinationDir.resolve("version").writeText("${project.version}")
            destinationDir.resolve("group").writeText("${project.group}")
        }
    }
}
