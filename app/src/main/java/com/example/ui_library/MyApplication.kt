package com.example.ui_library


import android.app.Application
import com.example.ui_library.util.ActivityManager

class MyApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        ActivityManager.instance.init(this)
    }

}