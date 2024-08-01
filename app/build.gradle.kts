plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.pixelfusion.accesio_utn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pixelfusion.accesio_utn"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //Dagger Hilt
    //implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.hilt.android)
    implementation(libs.firebase.firestore.ktx)
    //kapt("com.google.dagger:hilt-android-compiler:2.51.1")
    kapt(libs.hilt.android.compiler)
    //Retrofit
    //implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation(libs.retrofit)
    //Gson com.squareup.retrofit2:converter-gson
    //implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation(libs.converter.gson)
    //Pagination
    //implementation ("androidx.paging:paging-runtime:3.3.0")
    implementation (libs.androidx.paging.runtime.ktx)
    //implementation ("androidx.paging:paging-compose:3.3.0")
    implementation (libs.androidx.paging.compose)
    //Coil
    //implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation (libs.coil.compose)
    //ZXing
    //implementation ("com.google.zxing:core:3.5.3")
    implementation (libs.core)
    //implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation (libs.zxing.android.embedded)
    //Text from image
    //implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    implementation(libs.play.services.mlkit.text.recognition)

    //Constraint layout compose dependency
    //implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation(libs.androidx.constraintlayout.compose)
    // Import the Firebase BoM
    //implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation(platform(libs.firebase.bom))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    //implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.analytics)

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    //implementation("com.google.firebase:firebase-database")
    implementation(libs.firebase.database)
    //Auth
    //implementation ("com.google.firebase:firebase-auth")
    implementation(libs.google.firebase.auth)
    //storage
    //implementation("com.google.firebase:firebase-storage")
    implementation(libs.firebase.storage)
    //barcode-saning
    //implementation("com.google.mlkit:barcode-scanning:17.2.0")
    implementation(libs.barcode.scanning)
    //camera2
    implementation(libs.androidx.camera.camera2)
    //implementation("androidx.camera:camera-camera2:1.3.4")
    //implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation(libs.androidx.camera.lifecycle)
    //implementation("androidx.camera:camera-view:1.3.4")
    implementation(libs.androidx.camera.view)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    //implementation("androidx.compose.animation:animation:1.6.8")
    //implementation(libs.androidx.animation)
    //implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(libs.androidx.core.splashscreen)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

