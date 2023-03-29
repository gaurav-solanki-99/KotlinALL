package com.example.social_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.social_auth.Storage.PrefManager

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var token: String = PrefManager.getString(this, PrefManager.ACCESS_TOKEN).toString()
        println("token::::$token")

        Handler(Looper.getMainLooper()).postDelayed({
            checkstatus(token)
        },2000)
    }

    private fun checkstatus(token: String) {
        if (token != ""){
            startActivity(Intent(this@Splash,MainActivity::class.java))
        }else{
            startActivity(Intent(this@Splash,Login_activity::class.java))
        }
    }


}