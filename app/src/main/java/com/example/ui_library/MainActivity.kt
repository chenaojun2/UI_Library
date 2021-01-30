package com.example.ui_library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.ui_library.R
import com.example.ui_library.demo.ChTabBottomDemoActivity
import com.example.ui_library.demo.ChTabTopDemoActivity

/**
 *
 * */
class MainActivity : AppCompatActivity() , View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_tab_Bottom->{
                startActivity(Intent(this, ChTabBottomDemoActivity::class.java))
            }
            R.id.tv_tob_top->{
                startActivity(Intent(this, ChTabTopDemoActivity::class.java))
            }
        }
    }
}