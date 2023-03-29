package com.example.social_auth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class Facebook_data_Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_data)

        var name = intent.getStringExtra("name")
        var id = intent.getStringExtra("id")
        var picture = intent.getStringExtra("picture")


        findViewById<TextView>(R.id.facebookdata).text = name+"\n"+id
        var image = findViewById<ImageView>(R.id.facebookprofile)
        Glide.with(this@Facebook_data_Activity).load(picture).into(image)

    }
}