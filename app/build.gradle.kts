plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mislugares2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mislugares2"
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

}



dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.facebook.android:facebook-android-sdk:[8,9)")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}