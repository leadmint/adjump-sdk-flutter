
# AdJump Flutter SDK

AdJump is an offerwall monetization SDK that enables Flutter apps to launch a native Android offerwall. This README walks you through integrating and using AdJump within your Flutter project.

---

## 🚀 Features

- Easy integration with Flutter and native Android
- Simple method channel-based communication
- Automatic handling of SDK initialization and offerwall launch

---

## 🧰 Requirements

- Flutter SDK (2.10+ recommended)
- Android SDK (API 21+)
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
implementation("io.leadmint.adjump:offerwall:1.0.1")
```

Also ensure your app has internet permission in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🧑‍💻 Integration Guide

### 1. Android (Kotlin)

In your `MainActivity.kt` (inside `android/`):

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

        adJump = AdJump(this, "accountid", "appid", "userid")

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "launchOfferWall" -> {
                        launchOfferWall()
                        result.success("Offer wall launched")
                    }
                    else -> result.notImplemented()
                }
            }
    }

    private fun launchOfferWall() {
        adJump?.initialize(object : AdJump.InitialisationListener {
            override fun onInitialisationSuccess() {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "AdJump SDK Initialized", Toast.LENGTH_SHORT).show()
                }
                if (adJump?.isAvailable == true) {
                    adJump?.launchOfferWall()
                } else {
                    Toast.makeText(this@MainActivity, "Offerwall not available!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onInitialisationError(exception: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Initialization failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
```

### 2. Flutter Dart Code

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
            onPressed: _launchOfferWall,
            child: Text('Launch OfferWall'),
          ),
        ),
      ),
    );
  }

  Future<void> _launchOfferWall() async {
    try {
      await platform.invokeMethod('launchOfferWall');
    } on PlatformException catch (e) {
      print("Failed to show offer wall: '${e.message}'.");
    }
  }
}
```

---

## 🧪 Testing

- Make sure you're running on a physical Android device or emulator with internet access.
- Log output and Toast messages will indicate SDK initialization and errors.

---

## ❓ FAQ

**Q: Does it support iOS?**  
A: No, this SDK currently supports Android only.

**Q: Where do I get my `accountid`, `appid`, and `userid`?**  
A: These are provided by the AdJump platform once you're onboarded.

---

## 📞 Support

For help and support, contact your AdJump integration manager or mail at support@adjump.io

---

## 📃 License

Proprietary - For internal use only. Do not distribute without permission.
