plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
//    id("com.google.gms.google-services")
//    id("com.google.firebase.crashlytics")
    id("stringfog")
}
apply(plugin = "stringfog")
configure<com.github.megatronking.stringfog.plugin.StringFogExtension> {
    implementation = "com.github.megatronking.stringfog.xor.StringFogImpl"
    enable = true
    fogPackages = arrayOf("com.tqs.filecommander")
    kg = com.github.megatronking.stringfog.plugin.kg.RandomKeyGenerator()
    mode = com.github.megatronking.stringfog.plugin.StringFogMode.bytes
}

android {
    namespace = "com.tqs.filecommander"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.tqs.filecommander"
        minSdk = 24
        targetSdk = 33
        versionCode = 4
        versionName = "1.0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "TBAUrl",
                "\"https://introit.filecommender.com/stroll/too/thong/fountain\""
            )
            buildConfigField(
                "String",
                "CloakUrl",
                "\"https://collagen.filecommender.com/sinful/truk/yuck\""
            )
        }
        debug {
            buildConfigField(
                "String",
                "TBAUrl",
                "\"https://test-introit.filecommender.com/relict/schism/impiety\""
            )
            buildConfigField(
                "String",
                "CloakUrl",
                "\"https://collagen.filecommender.com/sinful/truk/yuck\""
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
        buildConfig = true
        dataBinding = true
    }
    android.applicationVariants.all {
        this.outputs.all {

        }
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    // gson
    implementation("com.google.code.gson:gson:2.10.1")
    // admob of google
    implementation("com.google.android.gms:play-services-ads:22.4.0")
//    implementation("com.google.android.ump:user-messaging-platform:2.1.0")
    // mmkv of tecent
    implementation("com.tencent:mmkv:1.2.14")
    implementation("com.blankj:utilcodex:1.31.0")
    // lottie anim
    implementation("com.airbnb.android:lottie:3.4.4")
    // firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
//    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    // reffer
    implementation("com.android.installreferrer:installreferrer:2.2")
    // StringFog
    implementation("com.github.megatronking.stringfog:xor:5.0.0")
    // RxJava
    implementation("io.reactivex:rxjava:1.1.3")
    // RxAndroid
    implementation("io.reactivex:rxandroid:1.1.0")
    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.0.0")
    // retrofit Gson
    implementation("com.squareup.retrofit2:converter-gson:2.0.0")
    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:3.2.0")
    // retrofit RxJava
    implementation("com.squareup.retrofit2:adapter-rxjava:2.0.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    //facebook
    implementation("com.facebook.android:facebook-android-sdk:12.3.0")
    //Google User Messaging Platform SDK
    implementation("com.google.android.ump:user-messaging-platform:2.1.0")

}