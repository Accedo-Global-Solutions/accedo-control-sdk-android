plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("jacoco")
}

group = "tv.accedo.one"
version = "1.3.6"

android {
    namespace = "tv.accedo.one.sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        buildConfigField("String", "MODULE_NAME", "\"${project.name}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["accedo_one_test_apikey"] = project.property("accedo_one_test_apikey")?.toString() ?: ""
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.annotation:annotation:1.9.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

apply {
    from("release-script.gradle")
}
