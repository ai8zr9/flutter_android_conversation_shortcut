import Flutter
import UIKit

public class SwiftAndroidConversationShortcutPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "android_conversation_shortcut", binaryMessenger: registrar.messenger())
    let instance = SwiftAndroidConversationShortcutPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
