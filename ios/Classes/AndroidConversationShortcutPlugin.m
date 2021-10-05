#import "AndroidConversationShortcutPlugin.h"
#if __has_include(<android_conversation_shortcut/android_conversation_shortcut-Swift.h>)
#import <android_conversation_shortcut/android_conversation_shortcut-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "android_conversation_shortcut-Swift.h"
#endif

@implementation AndroidConversationShortcutPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftAndroidConversationShortcutPlugin registerWithRegistrar:registrar];
}
@end
