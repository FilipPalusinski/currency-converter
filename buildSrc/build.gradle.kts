plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("transfergo-library-plugin") {
            id = "transfergo-library-plugin"
            implementationClass = "TransfergoLibraryPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    implementation("com.android.tools.build:gradle:${libs.versions.agp.get()}")
    implementation(kotlin("gradle-plugin", libs.versions.kotlin.get()))

    // to make Hilt compile when using buildSrc build logic
    implementation(libs.javapoet)
}
