# flutter_android_conversation_shortcut

A Simple Plugin that creates an android conversation shortcut for messaging style notifications, so that the notification will be displayed in the Conversations part of the notifications.  
*This plugin is intended as an enhancement to [flutter_local_notifications](https://pub.dev/packages/flutter_local_notifications).*

***Usage:***

Create a person object from the flutter_local_notification plugin's Person class

```dart
Person person = Person(
  key: '1',
  name: 'Bob',
  icon: ...,
  ...
);
```
Then create the shorcut with the provided function:

```dart
final shortcutId = await AndroidConversationShortcut.createConversationShortcut(person);
```
flutter_local_notifications provides an attribute for AndroidNotificationDetails that let's you specify the shortcutId. Additionally the styleInformation has to be a MessagingStyleInformation object:

```dart
AndroidNotificationDetails(
  channel.id,
  channel.name,
  channel.description,
  icon: '@drawable/is_notification',
  shortcutId: shortcutId,
  styleInformation: MessagingStyleInformation(
    person,
    groupConversation: false,
    messages: messages,
    ...
  ),
  ...
);
```
