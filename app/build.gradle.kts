plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.javacp"
    compileSdk = 34
    packaging {
        resources {
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/LICENSE.md"
        }
    }

    defaultConfig {
        applicationId = "com.example.javacp"
        minSdk = 24
        //noinspection OldTargetApi
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
      viewBinding =true
  }
}

dependencies {
    implementation(libs.firebase.auth)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.storage)
    implementation ("com.airbnb.android:lottie:6.1.0")
    dependencies {
        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)
        implementation(libs.firebase.auth)
        implementation(libs.firebase.firestore)
        implementation(libs.navigation.fragment)   // Navigation Component for Fragments
        implementation(libs.navigation.ui)         // Navigation UI utilities
        implementation(libs.lifecycle.common)
        implementation (libs.cloudinary.android)
        testImplementation(libs.junit)
        implementation (libs.glide) // Latest version
        annotationProcessor (libs.compiler)
        implementation ("com.razorpay:checkout:1.6.26")
        implementation("com.sun.mail:android-mail:1.6.7")
        implementation ("com.sun.mail:android-activation:1.6.7")
        androidTestImplementation(libs.ext.junit)
        implementation("com.google.android.exoplayer:exoplayer:2.19.1")

        androidTestImplementation(libs.espresso.core)
    }

}