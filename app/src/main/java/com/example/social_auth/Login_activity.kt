package com.example.social_auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.social_auth.Storage.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.properties.Delegates

class Login_activity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private var isactive by Delegates.notNull<Boolean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnLogin.setOnClickListener({
           val email = edtEmail.text.toString().trim()
           val password = edtPassword.text.toString().trim()

            if (email!= null || email!="" || password!= null || password!=""){
                login(email,password)
            }else{
                Toast.makeText(this@Login_activity,"Please Enter Email and Password..!!",Toast.LENGTH_SHORT).show()
            }

        })

        btnSignUp.setOnClickListener({
            startActivity(Intent(this@Login_activity,Sign_Up::class.java))
        })

    }

    private fun login(email: String, password: String) {



        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful){
                 isactive = true
                println("isactive=1"+isactive)
                PrefManager.setString(this@Login_activity, PrefManager.ACCESS_TOKEN,mAuth.uid)
                startActivity(Intent(this@Login_activity,MainActivity::class.java))
                finish()
            }else{
                isactive = false
                println("isactive=2"+isactive)

                Toast.makeText(this@Login_activity, "User does not exist", Toast.LENGTH_SHORT).show()
            }
        }
    }
}