package com.example.social_auth

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.social_auth.Adapter.UserAdapter
import com.example.social_auth.Model.User
import com.example.social_auth.Storage.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Chat_Activity : AppCompatActivity() {



    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
//    var token = PrefManager.getString(this@Chat_Activity,PrefManager.ACCESS_TOKEN).toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference //getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children) {
                    // get one user
                    val currentUser = postSnapshot.getValue(User::class.java)
                    // add user to list
                    if(mAuth.currentUser?.uid != currentUser?.uid ){
                        userList.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
             PrefManager.setString(this@Chat_Activity,PrefManager.ACCESS_TOKEN,"")

            // write the logic for logout
            mAuth.signOut()
            val intent = Intent(this@Chat_Activity, Login_activity::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}