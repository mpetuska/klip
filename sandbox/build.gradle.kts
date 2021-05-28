plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "1.5.10-1.0.0-beta01"
}

dependencies {
    ksp(project(":klip-processor"))
}

kotlin {
    jvm()
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(project(":klip-core"))
            }
        }
    }
}