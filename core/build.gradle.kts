plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()
    js {
        useCommonJs()
        nodejs()
    }
    sourceSets {
        named("commonMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:_")
            }
        }
        named("commonTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        named("jsMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:_")
            }
        }
    }
}