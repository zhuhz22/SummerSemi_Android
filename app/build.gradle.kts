plugins {
    id("com.android.application")
}

android {
    namespace = "com.java.zhuhongzhou"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.java.zhuhongzhou"
        minSdk = 28
        targetSdk = 33
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    implementation("io.github.scwang90:refresh-layout-kernel:2.0.6")
    implementation("io.github.scwang90:refresh-header-classics:2.0.6")
    implementation("io.github.scwang90:refresh-header-radar:2.0.6")
    implementation("io.github.scwang90:refresh-header-falsify:2.0.6")
    implementation("io.github.scwang90:refresh-header-material:2.0.6")
    implementation("io.github.scwang90:refresh-header-two-level:2.0.6")
    implementation("io.github.scwang90:refresh-footer-ball:2.0.6")
    implementation("io.github.scwang90:refresh-footer-classics:2.0.6")


    implementation ("androidx.core:core:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okio:okio:2.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}