# Adjump Flutter SDK

Adjump is an offerwall monetization SDK that enables Flutter apps to launch a native Android offerwall. This README walks you through integrating and using Adjump within your Flutter project.

---

## 🚀 Features

- Easy integration with Flutter and native Android
- Simple method channel-based communication
- Automatic handling of SDK initialization and offerwall launch
- Secure credential management
- Error handling and user feedback

---

## 🧰 Requirements

- Flutter SDK (2.10+ recommended)
- Android SDK (API 24+)
- Kotlin 1.5+
- Gradle 7.0+
- Internet permission

---

## 📦 Installation

### 1. Flutter Project Setup

No need to install a Flutter package. The communication is done using a `MethodChannel`.

### 2. Android Native Setup

Navigate to `android/app/build.gradle` and add this inside the `dependencies` block:

```groovy
implementation("io.leadmint.Adjump:offerwall:1.0.1")
```

Also ensure your app has internet permission in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🧑‍💻 Integration Guide

### 1. Flutter Implementation

In your `main.dart` file:

```dart
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  static const platform = MethodChannel('com.example.offerwall_app');

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: Text('AdJump Example')),
        body: Center(
          child: ElevatedButton(
            onPressed: () {
              _launchOfferWall();
            },
            child: Text('Launch OfferWall'),
          ),
        ),
      ),
    );
  }

  Future<void> _launchOfferWall() async {
    try {
      final Map<String, String> params = {
        'accountId': 'YOUR_ACCOUNT_ID',
        'appId': 'YOUR_APP_ID',
        'userId': 'USER_ID',
      };
      await platform.invokeMethod('launchOfferWall', params);
    } on PlatformException catch (e) {
      print("Failed to show offer wall: '${e.message}'.");
    }
  }
}
```

### 2. Android Implementation

In your `MainActivity.kt`:

```kotlin
package com.example.adjump

import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.adjump.offerwall.AdJump

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.example.offerwall_app"
    private var adJump: AdJump? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "launchOfferWall" -> {
                        val arguments = call.arguments as? Map<*, *>
                        if (arguments != null) {
                            val accountId = arguments["accountId"] as? String
                            val appId = arguments["appId"] as? String
                            val userId = arguments["userId"] as? String

                            if (accountId != null && appId != null && userId != null) {
                                adJump = AdJump(this, accountId, appId, userId)
                                launchOfferWall()
                                result.success("Offer wall launched")
                            } else {
                                result.error("INVALID_ARGUMENTS", "Missing required parameters", null)
                            }
                        } else {
                            result.error("INVALID_ARGUMENTS", "Arguments are required", null)
                        }
                    }
                    else -> result.notImplemented()
                }
            }
    }

    private fun launchOfferWall() {
        adJump?.initialize(object : AdJump.InitialisationListener {
            override fun onInitialisationSuccess() {
                runOnUiThread { Toast.makeText(this@MainActivity, "AdJump SDK Initialized", Toast.LENGTH_SHORT).show() }
                if (adJump?.isAvailable == true) {
                    adJump?.launchOfferWall()
                } else {
                    Toast.makeText(this@MainActivity, "Offerwall not available!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onInitialisationError(exception: Exception) {
                runOnUiThread { Toast.makeText(this@MainActivity, "Initialization failed: ${exception.message}", Toast.LENGTH_SHORT).show() }
            }
        })
    }
}
```

---

## 🔑 Configuration

1. Replace the placeholder values in your Flutter code with your actual Adjump credentials:
   - `YOUR_ACCOUNT_ID`: Your Adjump account identifier
   - `YOUR_APP_ID`: Your application identifier
   - `USER_ID`: The user identifier for the current session

2. Make sure the method channel name matches in both Flutter and Android code:
   ```dart
   static const platform = MethodChannel('com.example.offerwall_app');
   ```

---

## 🧪 Testing

- Make sure you're running on a physical Android device or emulator with internet access
- Log output and Toast messages will indicate:
  - SDK initialization status
  - Offerwall availability
  - Any errors that occur during the process

---

## ❓ FAQ

**Q: Does it support iOS?**  
A: No, this SDK currently supports Android only.

**Q: Where do I get my `accountId`, `appId`, and `userId`?**  
A: These are provided by the Adjump platform once you're onboarded.

**Q: What happens if the credentials are invalid?**  
A: The SDK will show an error message through a Toast notification and log the error details.

---

## 📞 Support

For help and support, contact your Adjump integration manager or mail at support@Adjump.io

---

## 📃 License

Proprietary - For internal use only. Do not distribute without permission. 