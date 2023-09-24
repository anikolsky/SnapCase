import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val localProperties = gradleLocalProperties(rootProject.projectDir)
val serverAddressProperty: String = localProperties.getProperty("serverAddress")
val versionFileProperty: String = localProperties.getProperty("appVersionFile")
val apkUpdateFileProperty: String = localProperties.getProperty("apkUpdateFile")
val apkDownloadedFileNameProperty: String = localProperties.getProperty("apkDownloadedFileName")
val firestoreWebClientId: String = localProperties.getProperty("firestoreWebClientId")

android {
    namespace = "com.omtorney.snapcase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.omtorney.snapcase"
        minSdk = 26
        targetSdk = 34
        versionCode = 7
        versionName = "0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "SERVER_ADDRESS", "\"$serverAddressProperty\"")
        buildConfigField("String", "VERSION_FILE", "\"$versionFileProperty\"")
        buildConfigField("String", "APK_UPDATE", "\"$apkUpdateFileProperty\"")
        buildConfigField("String", "DOWNLOADED_UPDATE", "\"$apkDownloadedFileNameProperty\"")
        buildConfigField("String", "FIRESTORE_CLIENT_ID", "\"$firestoreWebClientId\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
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
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    packaging {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    val composeBomVersion = "2023.09.01"
    val firebaseBomVersion = "32.3.1"
    val hiltComposeVersion = "1.0.0"
    val hiltVersion = "2.48"
    val hiltWork = "1.0.0"
    val lifecycleVersion = "2.6.2"
    val navVersion = "2.7.3"
    val roomVersion = "2.5.2"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material:1.5.1")

    implementation("androidx.compose.runtime:runtime-livedata:1.6.0-alpha06")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.airbnb.android:lottie-compose:6.1.0")

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
    implementation("io.github.azhon:appupdate:4.3.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBomVersion"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.room:room-testing:$roomVersion")

    // Instrumentation tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:truth:1.5.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

//    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}

kapt {
    correctErrorTypes = true
}
