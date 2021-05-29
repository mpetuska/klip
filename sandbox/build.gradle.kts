plugins {
//    id("com.google.devtools.ksp")
    id("dev.petuska.klip")
    kotlin("multiplatform")
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

kotlin {
    jvm()
    js {
        useCommonJs()
        nodejs()
    }
    sourceSets {
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}