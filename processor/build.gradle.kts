plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation("com.google.devtools.ksp:symbol-processing-api:_")
                implementation(project(":klip-core"))
            }
        }
    }
}