import 'dart:async';

import 'package:flutter/services.dart';

const int overlaySizeFill = -1;
const String _mainAppMethodChannel = "com.phan_tech./flutter_overlay_apps";
const String _overlayAppMethodChannel =
    "com.phan_tech/flutter_overlay_apps/overlay";
const String _overlayAppMessageChannel =
    "com.phan_tech/flutter_overlay_apps/overlay/messenger";

class FlutterOverlayApps {
  static const MethodChannel _channel = MethodChannel(_mainAppMethodChannel);

  // overlay methodChanel
  static const MethodChannel _overlayChannel =
      MethodChannel(_overlayAppMethodChannel);
  //Overlay BasicMessageChannel
  static const BasicMessageChannel _overlayMessageChannel =
      BasicMessageChannel(_overlayAppMessageChannel, JSONMessageCodec());

  /// Open overLay content
  /// Takes optional;
  ///   - int [height] default is [overlaySizeFill]
  ///   - int [width] default is [overlaySizeFill]
  ///   - OverlayAlignment [width] default is [alignment] [OverlayAlignment.center]
  static Future<bool?> showOverlay(
      {int height = overlaySizeFill,
      int width = overlaySizeFill,
      OverlayAlignment alignment = OverlayAlignment.center}) async {
    final bool? _res = await _channel.invokeMethod('showOverlay',
        {"height": height, "width": width, "alignment": alignment.name});
    return _res;
  }

  /// Closes overlau if open
  static Future<bool?> closeOverlay() async {
    final bool? _res = await _overlayChannel.invokeMethod('close');
    return _res;
  }

  /// broadcast data to and from overlay app
  /// the supported data type are;
  ///   - [int], [double], [bool], [String], null
  ///   - [List] of supported types
  ///   - [Map] of supported types
  static Future sendDataToAndFromOverlay(dynamic data) async {
    return await _overlayMessageChannel.send(data);
  }

  /// Streams message shared between overlay and main app
  static final StreamController _controller = StreamController();
  static StreamController overlayListener() {
    _overlayMessageChannel.setMessageHandler((message) async {
      _controller.add(message);
      return message;
    });
    return _controller;
  }

  /// dispose overlay controller
  static void disposeOverlayListener() {
    _controller.close();
  }
}

/// Overlay alignment on screen
enum OverlayAlignment {
  topLeft,
  topCenter,
  topRight,
  centerLeft,
  center,
  centerRight,
  bottomLeft,
  bottomCenter,
  bottomRight
}
