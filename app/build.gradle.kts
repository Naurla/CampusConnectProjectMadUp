plugins {
    id("com.android.application")
    kotlin("android")
    // Applies the Kotlin Annotation Processing Tool for Room
    kotlin("kapt")
}

android {
    namespace = "com.example.campusconnectprojectmad"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.campusconnectprojectmad"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Enable ViewBinding for easy access to layout elements
    buildFeatures {
        viewBinding = true
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
        // Must be set to 11 or higher to support Room/Kotlin
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Define the Room version
    val room_version = "2.6.1"
    val coroutines_version = "1.8.1"

    // Core Android Libraries
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Coroutines Libraries (Used for background database operations)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    // UI Libraries
    implementation("androidx.viewpager2:viewpager2:1.1.0")

    // Room Libraries
    implementation("androidx.room:room-runtime:$room_version")
    // 💡 FIX: This line was missing and is required for Coroutine support in Room DAOs
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}