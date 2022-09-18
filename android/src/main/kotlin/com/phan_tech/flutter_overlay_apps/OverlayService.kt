package com.phan_tech.flutter_overlay_apps

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.WindowManager
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.JSONMessageCodec
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class OverlayService : Service() {
    private var windowManager: WindowManager? = null
    private lateinit var flutterView: FlutterView
    private val flutterChannel = MethodChannel(FlutterEngineCache.getInstance().get("my_engine_id")!!.dartExecutor, overlayAppMethodChannel)
    private val overlayMessageChannel = BasicMessageChannel(FlutterEngineCache.getInstance().get("my_engine_id")!!.dartExecutor, overlayAppMessageChannel, JSONMessageCodec.INSTANCE)

    override fun onBind(intent: Intent?): IBinder? {
        // Not used
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val engine = FlutterEngineCache.getInstance().get("my_engine_id")!!
        engine.lifecycleChannel.appIsResumed()

        flutterView = FlutterView(applicationContext)

        flutterView.attachToFlutterEngine(FlutterEngineCache.getInstance().get("my_engine_id")!!)
        flutterView.fitsSystemWindows = true

        flutterChannel.setMethodCallHandler{ methodCall: MethodCall, result: MethodChannel.Result ->
            if(methodCall.method == "close"){
                val closed = stopService(Intent(baseContext, OverlayService().javaClass))
                result.success(closed)
            }
        }
        overlayMessageChannel.setMessageHandler(MyHandler())


        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager?

        val params = WindowManager.LayoutParams(
            WindowSetup.width,
            WindowSetup.height,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowSetup.flags,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = WindowSetup.gravity
        windowManager!!.addView(flutterView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager!!.removeView(flutterView)
    }
}

class MyHandler: BasicMessageChannel.MessageHandler<Any?>{
    override fun onMessage(message: Any?, reply: BasicMessageChannel.Reply<Any?>) {
        WindowSetup.messenger!!.send(message)
    }

}