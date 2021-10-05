import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:android_conversation_shortcut/android_conversation_shortcut.dart';

void main() {
  const MethodChannel channel = MethodChannel('android_conversation_shortcut');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await AndroidConversationShortcut.platformVersion, '42');
  });
}
