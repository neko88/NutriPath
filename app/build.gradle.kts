plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Use KSP for Room
    //id("com.google.devtools.ksp")
    id ("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.group35.nutripath"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.group35.nutripath"
        minSdk = 34
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.maps.android:android-maps-utils:2.3.0")
    implementation ("com.google.android.libraries.places:places:3.1.0")
    implementation ("com.google.maps.android:android-maps-utils:2.2.5")
    
    // database
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation (libs.androidx.lifecycle.livedata.ktx)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.androidx.core.splashscreen)

    // Camera Dependencies
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.video)
    implementation (libs.androidx.camera.extensions)

    // Barcode
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.barcode.scanning)
    implementation(libs.barcode.scanning.common)

    // API Caller
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.moshi)
    implementation (libs.moshi.kotlin)

    //ksp (libs.moshi.kotlin.codegen)

    // Glide Image Loader
    implementation (libs.glide.v4160)
    annotationProcessor (libs.compiler.v4160)

    // CSV Processing
    implementation (libs.opencsv)
    implementation (libs.gson)

    // Home menu
    implementation ("com.tbuonomo:dotsindicator:5.0")
    implementation ("com.google.android.material:material:1.12.0")

}