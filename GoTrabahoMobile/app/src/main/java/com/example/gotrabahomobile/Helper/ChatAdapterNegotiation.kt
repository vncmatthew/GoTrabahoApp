package com.example.gotrabahomobile.Helper

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.ChatActivity
import com.example.gotrabahomobile.ChatActivityNegotiation
import com.example.gotrabahomobile.Model.ChatRoom
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatAdapterNegotiation(private val context: Context, private val chatRoomList: ArrayList<ChatRoom>, private val serviceId: Int, private val serviceName: String?) :
    RecyclerView.Adapter<ChatAdapterNegotiation.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_chat_message, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatRoom = chatRoomList[position]


        val userIdToDisplay = if (FirebaseAuth.getInstance().currentUser?.uid == chatRoom.customerId) chatRoom.freelancerId else chatRoom.customerId
        getUserById(userIdToDisplay) { user ->
            if (user != null) {
                holder.txtUserName.text = "${user.firstName} ${user.lastName}"
                holder.layoutUser.setOnClickListener {
                    val intent = Intent(context, ChatActivityNegotiation::class.java)
                    intent.putExtra("serviceId", serviceId)
                    intent.putExtra("serviceName", serviceName)
                    intent.putExtra("userId", user.userId)
                    intent.putExtra("firstName", user.firstName)
                    intent.putExtra("lastName", user.lastName)
                    intent.putExtra("sqlId", user.sqlId)
                    intent.putExtra("chatroomId", chatRoom.chatroomId)
                    context.startActivity(intent)
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtUserName: TextView = view.findViewById(R.id.customerName)
        val layoutUser: LinearLayout = view.findViewById(R.id.layoutFreelancerMessage)
    }

    private fun getUserById(userId: String, callback: (UserFirebase?) -> Unit) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("UserFirebase").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserFirebase::class.java)
                callback(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}