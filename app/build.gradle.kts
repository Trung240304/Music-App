plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.musicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // Thêm phần này để sử dụng BuildConfig
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    dependencies {
        implementation("androidx.appcompat:appcompat:1.6.1") // Cập nhật phiên bản mới nhất
        implementation("com.google.android.material:material:1.10.0") // Cập nhật phiên bản mới nhất
        implementation("androidx.activity:activity:1.7.2") // Cập nhật phiên bản mới nhất
        implementation("androidx.constraintlayout:constraintlayout:2.1.4") // Cập nhật phiên bản mới nhất
        implementation("androidx.recyclerview:recyclerview:1.3.0") // Cập nhật phiên bản mới nhất
        implementation("androidx.gridlayout:gridlayout:1.0.0")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("de.hdodenhof:circleimageview:3.1.0")
        testImplementation("junit:junit:4.13.2")
        androidTestImplementation("androidx.test.ext:junit:1.1.5") // Cập nhật phiên bản mới nhất
        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") // Cập nhật phiên bản mới nhất
        implementation("com.github.bumptech.glide:glide:4.11.0")
        annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

    }

}
