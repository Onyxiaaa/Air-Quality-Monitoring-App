plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.iot_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.iot_project"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation ("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.4")
    implementation ("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    implementation ("com.github.AAChartModel:AAChartCore-Kotlin:-SNAPSHOT")
    implementation ("com.github.AAChartModel:AAChartCore-Kotlin:7.2.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.github.shuhart:material-calendar:1.1.0")
    implementation("jp.co.recruit_mp:LightCalendarView:1.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.6")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.6.1")
    implementation("com.squareup.retrofit2:converter-gson:2.6.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.0")
}
