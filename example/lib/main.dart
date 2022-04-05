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
      debugShowCheckedModeBanner: false, home: MyOverlaContent()));
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

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
                await FlutterOverlayApps.showOverlay(
                    height: 300,
                    width: 400,
                    alignment: OverlayAlignment.center);

                // send data to ovelay
                await Future.delayed(const Duration(seconds: 2));
                FlutterOverlayApps.sendDataToAndFromOverlay(
                    "Hello from main app");
              },
              child: const Text("showOverlay")),
        ),
      ),
    );
  }
}

class MyOverlaContent extends StatefulWidget {
  const MyOverlaContent({Key? key}) : super(key: key);

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
        onTap: () {
          // close overlay
          FlutterOverlayApps.closeOverlay();
        },
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(16),
          ),
          child: Center(
              child: Text(
            _dataFromApp,
            style: const TextStyle(color: Colors.red),
          )),
        ),
      ),
    );
  }
}
