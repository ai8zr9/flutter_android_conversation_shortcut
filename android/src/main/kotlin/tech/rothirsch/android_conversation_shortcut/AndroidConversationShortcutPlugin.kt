package tech.rothirsch.android_conversation_shortcut

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.*
import androidx.annotation.NonNull
import androidx.core.app.Person
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.io.File
import java.util.*

/** AndroidConversationShortcutPlugin */
class AndroidConversationShortcutPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var context: Context
  private lateinit var activity: Activity
  private lateinit var channel : MethodChannel

  private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int){
    outputStream().use { out ->
      bitmap.compress(format, quality, out)
      out.flush()
    }
  }

  private fun Bitmap.toCircle(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val path = Path().apply {
      addRoundRect(
              RectF(0f, 0f, width.toFloat(), height.toFloat()),
              width.toFloat() / 2,
              width.toFloat() / 2,
              Path.Direction.CCW
      )
    }
    canvas.clipPath(path)
    canvas.drawBitmap(this, 0f, 0f, null)
    return bitmap
  }

  private fun createRoundedIcon(filePath: String) {
    val image = File(filePath)
    val bmOptions = BitmapFactory.Options()
    val bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)
    val roundedBitmap = bitmap.toCircle()
    File(filePath).writeBitmap(roundedBitmap, Bitmap.CompressFormat.PNG, 100)
  }

  private fun createConversationShortcut(person: Person): String {
    val shortcut = ShortcutInfoCompat.Builder(context, person.key.toString() + "_sc")
            .setLongLived()
            .setIntent(Intent(Intent.ACTION_VIEW))
            .setPerson(person)
            .setShortLabel(person.name.toString())
            .build()
    ShortcutManagerCompat.addDynamicShortcuts(context, listOf(shortcut))
    return shortcut.id
  }

  private fun createPersonFromCall(@NonNull call: MethodCall): Person {
    if (call.argument<String>("personIcon") != null){
      createRoundedIcon(call.argument<String>("personIcon")!!)
    }
    val person =  Person.Builder()
            .setName(call.argument<String>("personName"))
            .setKey(call.argument("personKey"))
            .setBot(call.argument<Boolean>("personBot")!!)
            .setImportant(call.argument<Boolean>("personImportant")!!)
            .setUri(call.argument<String>("personUri"))
    if (call.argument<String>("personIcon") != null) {
      person.setIcon(IconCompat.createWithContentUri(call.argument<String>("personIcon")))
    }
    return person.build()
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "android_conversation_shortcut")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "createConversationShortcut") {
        val shortcutId = createConversationShortcut(createPersonFromCall(call))
        result.success(shortcutId)
    } else {
      result.notImplemented()
    }
  }


  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
  }

  override fun onDetachedFromActivity() {
  }
}
