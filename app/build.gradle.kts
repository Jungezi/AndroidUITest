plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.www233.uitest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.www233.uitest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    buildFeatures{
        dataBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.junit)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.lifecycle.extensions)
    implementation("androidx.room:room-runtime:2.2.5")
    annotationProcessor(libs.room.compiler)
    implementation("io.github.wuww233:GridPagerSnapHelper:1.0.1")
    implementation ("com.github.eekidu:devlayout:1.2.0")
}