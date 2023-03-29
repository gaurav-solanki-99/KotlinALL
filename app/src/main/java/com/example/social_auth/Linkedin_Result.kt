package com.example.social_auth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Linkedin_Result : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linkedin_result)
        var firstName = intent.getStringExtra("name")
        var lastName = intent.getStringExtra("last_name")
        var email = intent.getStringExtra("email")

        findViewById<TextView>(R.id.Texttlinkedin).text = firstName+"\t"+lastName+"\n"+email
    }
}