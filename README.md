# flutter_overlay_apps

Android plugin for displaying flutter app over other apps

## Usage

Add dependency to pubspec.yaml file


### Android
You'll need to add the `SYSTEM_ALERT_WINDOW` permission and `OverlayService` to your Android Manifest.
```XML
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <application>
        ...
        <service
           android:name="com.phan_tech.flutter_overlay_apps.OverlayService"
           android:exported="false" />
    </application>
```

### Entry point
Inside `main.dart` create an entry point for your Overlay widget;
NOTE: `MaterialApp` is required
```dart
// overlay entry point
@pragma("vm:entry-point")
void showOverlay() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: Material(child: Text("My overlay"))
  ));
}
```


### Methods
To open an overlay, call `FlutterOverlayApps.showOverlay()`. 
Default `height` & `width` is fill screen

```dart
FlutterOverlayApps.showOverlay(height: 300, width: 400, alignment: OverlayAlignment.center);
```

To close the overlay widget call 
```dart
FlutterOverlayApps.closeOverlay();
```
To send data to and from Overlay widget, call 
```dart
FlutterOverlayApps.sendDataToAndFromOverlay(<data>);
```
For listening to broadcasted data, stream the messages by calling 
```dart
FlutterOverlayApps.overlayListener().listen((data) {
    print(data);
});
```

### Code Example
```dart
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_overlay_apps/flutter_overlay_apps.dart';

void main() {
  runApp(const MyApp());
}

// overlay entry point
@pragma("vm:entry-point")
void showOverlay() {
  runApp(const MaterialApp(
    debugShowCheckedModeBanner: false,
    home: MyOverlaContent()
  ));
}


class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: () async {

              // Open overlay
              await FlutterOverlayApps.showOverlay(height: 300, width: 400, alignment: OverlayAlignment.center);

              // send data to ovelay
              await Future.delayed(const Duration(seconds: 2));
              FlutterOverlayApps.sendDataToAndFromOverlay("Hello from main app");
            }, 
            child: const Text("showOverlay")
          ),
        ),
      ),
    );
  }
}



class MyOverlaContent extends StatefulWidget {
  const MyOverlaContent({ Key? key }) : super(key: key);

  @override
  State<MyOverlaContent> createState() => _MyOverlaContentState();
}

class _MyOverlaContentState extends State<MyOverlaContent> {
  String _dataFromApp = "Hey send data";

  @override
  void initState() {
    super.initState();

    // lisent for any data from the main app
    FlutterOverlayApps.overlayListener().listen((event) {
      setState(() {
        _dataFromApp = event.toString();
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: InkWell(
        onTap: (){
          // close overlay
          FlutterOverlayApps.closeOverlay();
        },
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          child: Center(child: Text(_dataFromApp, style: const TextStyle(color: Colors.red),)),
        ),
      ),
    );
  }
}
```

Support the plugin <a href="https://www.buymeacoffee.com/EddieGenius" target="_blank"><img src="https://i.imgur.com/aV6DDA7.png" alt="Buy Me A Coffee" style="height: 41px !important;width: 174px !important; box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;-webkit-box-shadow: 0px 3px 2px 0px rgba(190, 190, 190, 0.5) !important;" > </a>
