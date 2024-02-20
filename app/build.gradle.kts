plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.emon.mycontactapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.emon.mycontactapp"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

// Access base URL from gradle.properties
val BASE_URL: String by project

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    //retrofit
    val retrofit = "2.9.0"
    val okHttp = "4.12.0"
    implementation("com.squareup.retrofit2:retrofit:${retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofit}")
    implementation("com.squareup.okhttp3:okhttp:${okHttp}")
    implementation("com.squareup.okhttp3:logging-interceptor:${okHttp}")

    //gson
    val gson = "2.10.1"
    implementation("com.google.code.gson:gson:${gson}")

    //lifecycle
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Navigation
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    //Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    //circleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")
}