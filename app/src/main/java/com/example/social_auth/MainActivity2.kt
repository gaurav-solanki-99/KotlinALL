package com.example.social_auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var email = intent.getStringExtra("email")
        var name = intent.getStringExtra("name")

        findViewById<TextView>(R.id.Texttgoogle).text = email+"\n"+name
    }
}