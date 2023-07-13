package com.phan_tech.flutter_overlay_apps

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.WindowManager
import io.flutter.plugin.common.BasicMessageChannel

object WindowSetup {
    var height: Int = -1
    var width: Int = WindowManager.LayoutParams.MATCH_PARENT
    var gravity: Int = Gravity.CENTER
    var messenger : BasicMessageChannel<Any?>? = null
    var closeOnBackButton : Boolean = true;

    @SuppressLint("RtlHardcoded")
    fun setGravityFromAlignment(alignment: String){
        when {
            alignment.lowercase() == "topLeft".lowercase() -> {
                gravity = Gravity.TOP or Gravity.LEFT
            }
            alignment.lowercase() == "topCenter".lowercase() -> {
                gravity = Gravity.TOP
            }
            alignment.lowercase() == "topRight".lowercase() -> {
                gravity = Gravity.TOP or Gravity.RIGHT
            }


            alignment.lowercase() == "centerLeft".lowercase() -> {
                gravity = Gravity.CENTER or Gravity.LEFT
            }
            alignment.lowercase() == "center".lowercase() -> {
                gravity = Gravity.CENTER
            }
            alignment.lowercase() == "centerRight".lowercase() -> {
                gravity = Gravity.CENTER or Gravity.RIGHT
            }


            alignment.lowercase() == "bottomLeft".lowercase() -> {
                gravity = Gravity.BOTTOM or Gravity.LEFT
            }
            alignment.lowercase() == "bottomCenter".lowercase() -> {
                gravity = Gravity.BOTTOM
            }
            alignment.lowercase() == "bottomRight".lowercase() -> {
                gravity = Gravity.BOTTOM or Gravity.RIGHT
            }


        }
    }
}