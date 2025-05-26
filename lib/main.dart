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
