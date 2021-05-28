plugins {
    `java-gradle-plugin`
    kotlin("jvm")
}

gradlePlugin {
    plugins {
        create(name) {
            id = "$group.$name"
            implementationClass = "$group.KlipPlugin"
        }
    }
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(project(":klip-processor"))

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
