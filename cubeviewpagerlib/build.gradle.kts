plugins {
    id("com.android.library")

}

android {
    namespace = "com.example.cubeviewpagerlib"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.viewpager2)
    implementation(libs.glide)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

}
