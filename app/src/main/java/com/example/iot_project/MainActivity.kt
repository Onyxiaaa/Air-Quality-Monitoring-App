package com.example.iot_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    lateinit var button1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button()
    }
    private fun button(){
    button1 = findViewById(R.id.getstart)
    button1.setOnClickListener{
        val pindah = Intent(this, nav::class.java)
        startActivity(pindah)
        }
    }

}