import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_overlay_apps/flutter_overlay_apps.dart';

void main() {
  const MethodChannel channel =
      MethodChannel('com.phan_tech./flutter_overlay_apps');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return true;
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await FlutterOverlayApps.showOverlay(), true);
  });
}
