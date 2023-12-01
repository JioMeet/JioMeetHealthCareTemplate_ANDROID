# JioMeet Template UI Quickstart

**Welcome to JioHealthCareTemplate Template UI**, a SDK that streamlines the integration of Jiomeet's powerful audio and video functionalities.

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Prerequisites](#prerequisites)
   - [Jetpack Compose](#jetpack-compose)
4. [Setup](#setup)
   - [Register on JioMeet Platform](#register-on-jiomeet-platform)
   - [Get Your Application Keys](#get-your-application-keys)
   - [Get Your JioMeet Meeting ID and PIN](#get-your-jiomeet-meeting-id-and-pin)
5. [Configure JioMeet Template UI Inside Your App](#configure-jiomeet-template-ui-inside-your-app)
   - [Add permissions for network and device access](#add-permissions-for-network-and-device-access)
   - [Start Your App](#start-your-app)
6. [Sample App](#sample-app)
7. [Troubleshooting](#troubleshooting)

## Introduction

In this documentation, we'll guide you through the process of installation, enabling you to enhance your Android app with Jiomeet's real-time communication capabilities swiftly and efficiently.Let's get started on your journey to creating seamless communication experiences with Jiomeet Template UI!
---

## Features

In Jiomeet Template UI, you'll find a range of powerful features designed to enhance your Android application's communication and collaboration capabilities. These features include:

**Voice and Video Calling**:Enjoy high-quality, real-time audio and video calls with your contacts.

## Prerequisites

Before you begin, ensure you have met the following requirements:

#### Dependencies to be added

   ``` gradle
   plugins {
     kotlin("kapt")
   }

   android {
       ...
       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_11
           targetCompatibility = JavaVersion.VERSION_11
       }
   }
```

#### Jetpack Compose:

JioMeet Template UI relies on Jetpack Compose for its user interface components.
Ensure that your Android project is configured to use Jetpack Compose. You can add the necessary configurations to your project's build.gradle file:

```gradle
  // Enable Jetpack Compose
  buildFeatures {
      compose true
  }

  // Set the Kotlin compiler extension version for Compose
  composeOptions {
      kotlinCompilerExtensionVersion = "1.3.2"
  }
```

---

## Setup

##### Register on JioMeet Platform:

You need to first register on Jiomeet platform.[Click here to sign up](https://platform.jiomeet.com/login/signUp)

##### Get your application keys:

Create a new app. Please follow the steps provided in the [Documentation guide](https://dev.jiomeet.com/docs/quick-start/introduction) to create apps before you proceed.

###### Get you Jiomeet meeting id and pin

Use the [create meeting api](https://dev.jiomeet.com/docs/JioMeet%20Platform%20Server%20APIs/create-a-dynamic-meeting) to get your room id and password

## Configure JioMeet Template UI inside your app

i. In Gradle Scripts/build.gradle (Module: <projectname>) add the Template UI dependency. The dependencies section should look like the following:

```gradle
dependencies {
    ...
    implementation "com.jiomeet.platform:jiomeetcoretemplatesdk:<version>"
    ...
}
```

Find the [Latest version](https://github.com/JioMeet/JioMeetCoreTemplateSDK_ANDROID/releases) of the UI Kit and replace <version> with the one you want to use. For example: 2.1.8.

### Add permissions for network and device access.

In /app/Manifests/AndroidManifest.xml, add the following permissions after </application>:

```gradle
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- The SDK requires Bluetooth permissions in case users are using Bluetooth devices. -->
<uses-permission android:name="android.permission.BLUETOOTH" />
<!-- For Android 12 and above devices, the following permission is also required. -->
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
```

### Requesting run time permissions

it's crucial to request some permissions like **_CAMERA ,RECORD_AUDIO, READ_PHONE_STATE_** at runtime since these are critical device access permissins to ensure a seamless and secure user experience. Follow these steps

1. Check Permissions

```kotlin
if (checkPermissions()) {
    // Proceed with using the features.
} else {
    // Request critical permissions at runtime.
}
```

2. Request Runtime Permissions:

```kotlin
private void requestCriticalPermissions() {
    ActivityCompat.requestPermissions(this,
        new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        },
        PERMISSION_REQUEST_CODE);

}
```

3. Handle Permission Results

```kotlin
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == PERMISSION_REQUEST_CODE) {
        if (areAllPermissionsGranted(grantResults)) {
            // Proceed with using the features that require critical permissions.
        } else {
            // Handle denied permissions, especially for camera and phone state, which are essential.
        }
    }
}
```

### Start your App

1. Modify AndroidManifest.xml: In the AndroidManifest.xml file of their app, users should specify the custom Application class they created as the application name. This tells Android to use their custom Application class when the app starts.

```xml
<application
    android:name=".MyApplication" <!-- Specify the name of your custom Application class -->
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/AppTheme">
    <!-- ... -->
</application>
```

### 

2. Update xml file for parent activity add below parameters.
    **Note**:- This would be the parent view where you will launch your template.
    ```xml
    <androidx.compose.ui.platform.ComposeView
    android:id="@+id/compose_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
    ```
3. update onCreate to run LaunchCore() when the app starts. The updated code should be like the provided code sample:
```kotlin 
    private val isPipEnabled = MutableStateFlow(false)
    private val pipSupported: Boolean by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) packageManager.hasSystemFeature(
            PackageManager.FEATURE_PICTURE_IN_PICTURE
        )
        else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUrl.initializedNetworkInformation(this@JoinRoomActivity, Constant.Environment.PROD)

        val jioMeetListener = object : JioMeetListener {
            override fun onLeaveMeeting() {
                finish()
                Toast.makeText(this@JoinRoomActivity, "Left meeting ", Toast.LENGTH_LONG).show()
            }

            override fun onParticipantIconClicked() {
                Toast.makeText(this@JoinRoomActivity, "partcipant Icon clicked ", Toast.LENGTH_LONG)
                    .show()
            }
        }
        val jmJoinMeetingData = JMJoinMeetingData(
            meetingId = MEETIND_ID
            meetingPin = MEETING_PIN,
            displayName = DISPLAY_NAME,
            version = "",
            deviceId = ""
        )
        setContent {
            LaunchJioHealthCare(
                jioMeetListener = jioMeetListener,
                jmJoinMeetingData = jmJoinMeetingData,
                isPipEnabled = isPipEnabled
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isPipEnabled.value = isInPictureInPictureMode
    }


    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (!pipSupported) return
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        pipParams()?.let { enterPictureInPictureMode(it) }
    }
}
```
### Sample app

Visit our [JiomeetHealthCareTemplate UI Sample app](https://github.com/JioMeet/JioMeetHealthCareTemplate_ANDROID) repo to run the ample app.

---

## Troubleshooting

- Facing any issues while integrating or installing the JioMeet Template UI Kit please connect with us via real time support present in jiomeet.support@jio.com or https://jiomeetpro.jio.com/contact-us

---
