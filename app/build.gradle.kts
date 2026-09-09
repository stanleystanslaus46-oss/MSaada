
plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.devtools.ksp)
  alias(libs.plugins.roborazzi)
}

android {
  namespace = "com.example"
  compileSdk { version = release(36) { minorApiLevel = 1 } }

  defaultConfig {
    applicationId = "com.aistudio.msaada.tzapp"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    val paymentApiUrl = System.getenv("MSAADA_PAYMENT_API_URL")?.trim().orEmpty()
    val paymentNumber = System.getenv("MSAADA_PAYMENT_NUMBER")?.trim().takeUnless { it.isNullOrBlank() } ?: "+255742259683"
    val activationApiUrl = System.getenv("MSAADA_ACTIVATION_API_URL")?.trim().orEmpty()
    buildConfigField("String", "MSAADA_PAYMENT_API_URL", "\"${paymentApiUrl.replace("\\", "\\\\").replace("\"", "\\\"")}\"")
    buildConfigField("String", "MSAADA_PAYMENT_NUMBER", "\"${paymentNumber.replace("\\", "\\\\").replace("\"", "\\\"")}\"")
    buildConfigField("String", "MSAADA_ACTIVATION_API_URL", "\"${activationApiUrl.replace("\\", "\\\\").replace("\"", "\\\"")}\"")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    create("release") {
      // Release signing is optional in the source tree. Configure it through
      // environment variables when producing a signed release build.
      val keystorePath = System.getenv("KEYSTORE_PATH")
      val storePasswordEnv = System.getenv("STORE_PASSWORD")
      val keyPasswordEnv = System.getenv("KEY_PASSWORD")
      val keystoreFile = keystorePath?.let(::file)

      if (keystoreFile != null && keystoreFile.exists() &&
        !storePasswordEnv.isNullOrBlank() && !keyPasswordEnv.isNullOrBlank()) {
        storeFile = keystoreFile
        storePassword = storePasswordEnv
        keyAlias = System.getenv("KEY_ALIAS") ?: "upload"
        keyPassword = keyPasswordEnv
      }
    }
  }

  buildTypes {
    release {
      isCrunchPngs = false
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      // If release signing environment variables are not supplied, Android Studio
      // can sign the APK/AAB through its standard "Generate Signed Bundle / APK" flow.
      val hasReleaseSigning = System.getenv("KEYSTORE_PATH")?.let(::file)?.exists() == true &&
        !System.getenv("STORE_PASSWORD").isNullOrBlank() &&
        !System.getenv("KEY_PASSWORD").isNullOrBlank()
      if (hasReleaseSigning) {
        signingConfig = signingConfigs.getByName("release")
      }
    }
    debug {
    // Use Android Gradle Plugin's standard debug keystore.
    // No private debug keystore is required in the repository.
  }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  testOptions { unitTests { isIncludeAndroidResources = true } }
  dependenciesInfo {
    includeInApk = false
    includeInBundle = true
  }
}

// Some unused dependencies are commented out below instead of being removed.
// This makes it easy to add them back in the future if needed.
dependencies {
  implementation(platform(libs.androidx.compose.bom))
  // implementation(libs.accompanist.permissions)
  implementation(libs.androidx.activity.compose)
  // implementation(libs.androidx.camera.camera2)
  // implementation(libs.androidx.camera.core)
  // implementation(libs.androidx.camera.lifecycle)
  // implementation(libs.androidx.camera.view)
  implementation(libs.androidx.compose.material.icons.core)
  implementation(libs.androidx.compose.material.icons.extended)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  // implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.room.ktx)
  implementation(libs.androidx.room.runtime)
  implementation(libs.coil.compose)
  implementation(libs.zxing.core)
  // Uncomment to use Firestore:
  // implementation(libs.firebase.firestore)

  // Uncomment ALL FOUR of the following dependencies together to use Firebase Auth and Google
  // Sign-In via Credential Manager:
  // implementation(libs.firebase.auth)
  // implementation(libs.androidx.credentials)
  // implementation(libs.androidx.credentials.play.services)
  // implementation(libs.googleid)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.coroutines.core)
  // implementation(libs.play.services.location)
  testImplementation(libs.androidx.compose.ui.test.junit4)
  testImplementation(libs.androidx.core)
  testImplementation(libs.androidx.junit)
  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.roborazzi.compose)
  testImplementation(libs.roborazzi.junit.rule)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.runner)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
  "ksp"(libs.androidx.room.compiler)
}
