import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
val apiKey: String = localProperties.getProperty("API_KEY")
val baseUrl: String = localProperties.getProperty("BASE_URL")

android {
    namespace = "com.perihan.newsreaderandroid.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    //Retrofit + Kotlinx Serialization
    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)

    //OkHttp
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.logging.interceptor)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Paging
    implementation(libs.paging.runtime)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}