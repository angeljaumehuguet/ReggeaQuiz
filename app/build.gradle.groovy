plugins {
    id 'com.android.application'
}

android {
    compileSdk 33

    defaultConfig {
        applicationId "com.example.reggeaquiz"
        minSdk 21
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        // Room schema location
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += ["room.schemaLocation": "$projectDir/schemas".toString()]
            }
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding true
    }
}

dependencies {
    // Android Core
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    
    // Navigation Component
    implementation 'androidx.navigation:navigation-fragment:2.5.3'
    implementation 'androidx.navigation:navigation-ui:2.5.3'
    
    // Room components
    implementation 'androidx.room:room-runtime:2.4.3'
    annotationProcessor 'androidx.room:room-compiler:2.4.3'
    
    // Lifecycle components
    implementation 'androidx.lifecycle:lifecycle-viewmodel:2.5.1'
    implementation 'androidx.lifecycle:lifecycle-livedata:2.5.1'
    implementation 'androidx.lifecycle:lifecycle-common-java8:2.5.1'
    
    // CardView
    implementation 'androidx.cardview:cardview:1.0.0'
    
    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.4'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0'
}
