package com.phan_tech.flutter_overlay_apps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.dart.DartExecutor

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.*
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

const val mainAppMethodChannel: String = "com.phan_tech./flutter_overlay_apps"
const val overlayAppMethodChannel: String = "com.phan_tech/flutter_overlay_apps/overlay"
const val overlayAppMessageChannel: String = "com.phan_tech/flutter_overlay_apps/overlay/messenger"

/** FlutterOverlayAppsPlugin */
class FlutterOverlayAppsPlugin: FlutterPlugin, MethodCallHandler, ActivityAware, BasicMessageChannel.MessageHandler<Any?> {
  private lateinit var channel : MethodChannel
  private lateinit var messenger : BasicMessageChannel<Any?>
  private lateinit var context: Context
  private lateinit var activity: Activity

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    WindowSetup.messenger = messenger
    WindowSetup.messenger!!.setMessageHandler(this)
    activity = binding.activity
    val enn = FlutterEngineGroup(context)
    val dEntry = DartExecutor.DartEntrypoint(
      FlutterInjector.instance().flutterLoader().findAppBundlePath(),
      "showOverlay")

    val engine = enn.createAndRunEngine(context, dEntry)

    FlutterEngineCache.getInstance().put("my_engine_id", engine)

  }

  override fun onDetachedFromActivityForConfigChanges() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
  }

  override fun onDetachedFromActivity() {
  }

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, mainAppMethodChannel)
    channel.setMethodCallHandler(this)

    messenger = BasicMessageChannel(flutterPluginBinding.binaryMessenger, overlayAppMessageChannel, JSONMessageCodec.INSTANCE)
    messenger.setMessageHandler(this)
    this.context = flutterPluginBinding.applicationContext

  }



  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "showOverlay" -> {

        // get permissions
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
          result.error("1","SDK version is lower than 23", "Requires android sdk 23 and above")
        }
        else if(!checkPermissions()){
          requestPermissions()
        }else{
          val h = call.argument<Int>("height")
          val w = call.argument<Int>("width")
          val alignment = call.argument<String>("alignment")

          WindowSetup.width = w ?: -1
          WindowSetup.height = h ?: -1
          WindowSetup.setGravityFromAlignment(alignment ?: "center")
          activity.startService(Intent(context, OverlayService().javaClass))
          result.success(true)
        }


      }
//      "closeOverlay" -> {
//        val overlayChannel = MethodChannel(FlutterEngineCache.getInstance().get("my_engine_id")!!.dartExecutor, "com.phan_tech/overlay")
//        overlayChannel.invokeMethod("close", null)
//      }
      else -> {
        result.notImplemented()
      }
    }
  }

  override fun onMessage(message: Any?, reply: BasicMessageChannel.Reply<Any?>) {
    val overlayMessageChannel = BasicMessageChannel(FlutterEngineCache.getInstance().get("my_engine_id")!!.dartExecutor, overlayAppMessageChannel, JSONMessageCodec.INSTANCE)
    overlayMessageChannel.send(message, reply)
  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun checkPermissions(): Boolean{
    return Settings.canDrawOverlays(context)

  }

  @RequiresApi(Build.VERSION_CODES.M)
  private fun requestPermissions(){
    activity.startActivity(
      Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${activity.packageName}"))
    )
  }



}
