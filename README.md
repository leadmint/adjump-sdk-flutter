# 📱 AdJump Flutter SDK

AdJump is an offerwall SDK that enables monetization by presenting users with offers directly within your Flutter app. This SDK bridges Flutter and native Android using a `MethodChannel`.

## 🧩 Features

- Launch a native Android offerwall from Flutter
- Customizable through native configuration
- Simple setup with MethodChannel integration

---

## 📦 Installation

1. Add the following dependencies in your Flutter project:

```yaml
# pubspec.yaml
dependencies:
  flutter:
    sdk: flutter
```

2. Add the native Android SDK in your `MainActivity.kt` (already integrated in this example).

---

## 🛠️ Setup

### 1. Android Native Integration (`MainActivity.kt`)

```kotlin
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

### 2. Flutter Integration (`main.dart`)

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
      await platform.invokeMethod('launchOfferWall');
    } on PlatformException catch (e) {
      print("Failed to show offer wall: '${e.message}'.");
    }
  }
}
```

---

## 🔄 MethodChannel Methods

| Method Name        | Description                    |
|--------------------|--------------------------------|
| `launchOfferWall`  | Initializes SDK and shows wall |

---

## 📌 Notes

- This SDK only works on Android at the moment.
- Make sure the `accountid`, `appid`, and `userid` are set correctly in `MainActivity.kt`.
