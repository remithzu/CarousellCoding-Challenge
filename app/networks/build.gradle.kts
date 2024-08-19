plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt.plugin)
}
val propertiesFile = file("../config/endpoint.properties")
android {
    namespace = "robi.codingchallenge.networks"
    compileSdk = 34
    buildFeatures.buildConfig = true

    defaultConfig {
        minSdk = 24
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            if (propertiesFile.exists()) {
                propertiesFile.forEachLine {
                    val (key, value) = it.split("=")
                    if (key.isNotBlank() && value.isNotBlank()) {
                        buildConfigField("String", key, value)
                    }
                }
            } else {
                throw GradleException("Properties file not found: $propertiesFile")
            }
            isMinifyEnabled = false
        }
        release {
            if (propertiesFile.exists()) {
                propertiesFile.forEachLine {
                    val (key, value) = it.split("=")
                    if (key.isNotBlank() && value.isNotBlank()) {
                        buildConfigField("String", key, value)
                    }
                }
            } else {
                throw GradleException("Properties file not found: $propertiesFile")
            }
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":app:library"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.devtools.ksp)
    implementation(libs.google.hilt.android)
    implementation(libs.io.reactivex.rxjava)
    implementation(libs.io.reactivex.rxandroid)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.adapter.rxjava3)
    implementation(libs.squareup.converter.gson)
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.logging.interceptor)
    implementation(libs.multidex)
    ksp(libs.google.hilt.compiler)
    ksp(libs.google.dagger.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}