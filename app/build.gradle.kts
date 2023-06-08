import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

//val localProperties = gradleLocalProperties(rootProject.projectDir)

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.omtorney.snapcase"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.omtorney.snapcase"
        minSdk = 26
        targetSdk = 33
        versionCode = 6
        versionName = "0.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = gradleLocalProperties(rootProject.projectDir)
        val serverAddressProperty = localProperties.getProperty("serverAddress")
        val versionFileProperty = localProperties.getProperty("appVersionFile")
        val apkUpdateFileProperty = localProperties.getProperty("apkUpdateFile")
        val apkDownloadedFileNameProperty = localProperties.getProperty("apkDownloadedFileName")
        buildConfigField("String", "SERVER_ADDRESS", "\"$serverAddressProperty\"")
        buildConfigField("String", "VERSION_FILE", "\"$versionFileProperty\"")
        buildConfigField("String", "APK_UPDATE", "\"$apkUpdateFileProperty\"")
        buildConfigField("String", "DOWNLOADED_UPDATE", "\"$apkDownloadedFileNameProperty\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
//            val serverAddressProperty = localProperties.getProperty("serverAddress") ?: "empty"
//            buildConfigField("String", "SERVER_ADDRESS", "\"$serverAddressProperty\"")

            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    packaging {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    val composeUiVersion = "1.4.3"
    val hiltComposeVersion = "1.0.0"
    val hiltVersion = "2.46.1"
    val hiltWork = "1.0.0"
    val lifecycleVersion = "2.6.1"
    val navVersion = "2.5.3"
    val roomVersion = "2.5.1"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:$composeUiVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeUiVersion")
    implementation("androidx.compose.material3:material3:1.1.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.5.0-beta01")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
//    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.airbnb.android:lottie-compose:6.0.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$hiltComposeVersion")

    // Hilt Work
    implementation("androidx.hilt:hilt-work:$hiltWork")
    kapt("androidx.hilt:hilt-compiler:$hiltWork")

    // Room
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Work
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // AppUpdate
    implementation("io.github.azhon:appupdate:4.2.9")

    // Firebase
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.3.7")

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.room:room-testing:$roomVersion")

    // Instrumentation tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:truth:1.5.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeUiVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeUiVersion")

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}

kapt {
    correctErrorTypes = true
}