package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.FreelancerChatAdapter
import com.example.gotrabahomobile.Model.Chat
import com.example.gotrabahomobile.Model.UserFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging

class FreelancerMessagesActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    var userList = ArrayList<UserFirebase>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freelancer_messages)

        userRecyclerView = findViewById(R.id.rvFreelancerMessageList)

        //val imgBack: ImageView = findViewById(R.id.imgBack)


        userRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

/*        imgBack.setOnClickListener {
            onBackPressed()
        }*/

        getUsersList()
    }

    fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        var userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("UserFirebase")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("DEBUG", "Path: ${dataSnapshot.ref.path}")
                Log.d("DEBUG", "Data: ${dataSnapshot.value}")
                val user = dataSnapshot.getValue(UserFirebase::class.java)
                Log.d("DEBUG", "User: $user")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("DEBUG", "Failed to read value.", databaseError.toException())
            }
        })

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(UserFirebase::class.java)
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    Log.d("TAG", "Value is: $user")
                    if (!user!!.userId.equals(firebase.uid)) {
                        Log.d("List of Users", "Value is: $user")
                            Log.d("List of Active Users", "Value is: $user")
                            userList.add(user)

                    }
                }

                val userAdapter = FreelancerChatAdapter(this@FreelancerMessagesActivity, userList)
                userRecyclerView = findViewById(R.id.rvFreelancerMessageList)
                userRecyclerView.adapter = userAdapter
            }

        })
    }
    }
