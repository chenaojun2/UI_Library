package com.example.ui_library

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ui_library.util.ActivityManager
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val topActivity:Activity ? = ActivityManager.instance.topActivity

        if(topActivity != null){
            content.setText(topActivity.localClassName)
        }
    }
}