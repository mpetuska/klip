plugins {
    kotlin("multiplatform")
    id("com.google.devtools.ksp") version "1.5.10-1.0.0-beta01"
    idea
}

dependencies {
    ksp(project(":klip-processor"))
}

ksp {
    arg("klip.root", projectDir.resolve("src").canonicalPath)
}

kotlin {
    jvm()
    sourceSets {
        named("jvmTest") {
            dependencies {
                implementation(project(":klip-core"))
                implementation(kotlin("test"))
            }
        }
        all {
            val klipRoot = projectDir.resolve("src/$name/klips").canonicalPath
            resources.srcDir(klipRoot)
            ksp {
                arg("klip.root.$name", klipRoot)
            }
        }
    }
}