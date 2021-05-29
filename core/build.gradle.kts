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
                api(kotlin("test"))
                api(kotlin("test-annotations-common"))
            }
        }
        named("jvmMain") {
            dependencies {
            }
        }
        named("jsMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:_")
            }
        }
    }
}