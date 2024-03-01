package com.example.gotrabahomobile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.ChatAdapter
import com.example.gotrabahomobile.Model.Chat
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

@Suppress("DEPRECATION")
class ChatActivity : AppCompatActivity() {

    private lateinit var etMessage: EditText
    private lateinit var btnSendMessage: ImageButton
    private lateinit var btnSetPrice: Button
//    private lateinit var imgBack: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvSetPrice:TextView
    private lateinit var chatRecyclerView: RecyclerView
    private var userData: UserFirebase? = null

    var topic = ""
    var chatList = ArrayList<Chat>()

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        etMessage = findViewById(R.id.etMessage)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        tvUserName = findViewById(R.id.tvUserName)
        btnSetPrice = findViewById(R.id.buttonChatSetPrice)

        //get UserModel

            var intent = getIntent()
            var Id = intent.getIntExtra("serviceId", 0)
            var userId = intent.getStringExtra("userId")
            var firstName = intent.getStringExtra("firstName")
            var lastName = intent.getStringExtra("lastName")

            tvUserName.setText(firstName + " " + lastName)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("UserFirebase").child(userId!!)




        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(UserFirebase::class.java)
                tvUserName.text = (user!!.firstName+ " " + user!!.lastName)
            }
        })

        btnSendMessage.setOnClickListener {
            var message: String = etMessage.text.toString()

            if (message.isEmpty()) {
                Toast.makeText(applicationContext, "message is empty", Toast.LENGTH_SHORT).show()
                etMessage.setText("")
            } else {
                sendMessage(firebaseUser!!.uid, userId, message)
                etMessage.setText("")
                topic = "/topics/$userId"
                }

            }
        readMessage(firebaseUser!!.uid, userId)

        btnSetPrice.setOnClickListener{
            showDialog()

        }

        }




    private fun sendMessage(senderId: String, receiverId: String, message: String) {
        var reference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

        var hashMap: HashMap<String, String> = HashMap()
        hashMap.put("senderId", senderId)
        hashMap.put("receiverId", receiverId)
        hashMap.put("message", message)

        reference!!.child("Chat").push().setValue(hashMap)

    }

    fun readMessage(senderId: String, receiverId: String) {
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)

                    if (chat!!.senderId.equals(senderId) && chat!!.receiverId.equals(receiverId) ||
                        chat!!.senderId.equals(receiverId) && chat!!.receiverId.equals(senderId)
                    ) {
                        chatList.add(chat)
                    }
                }

                val chatAdapter = ChatAdapter(this@ChatActivity, chatList)

                chatRecyclerView.adapter = chatAdapter
            }
        })
    }

    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.set_price_dialog, null)

        // Find the close button from the dialogView
        val closeButton = dialogView.findViewById<ImageButton>(R.id.close_button)

        // Find the tvSetPrice from the dialogView
        val tvSetPrice = dialogView.findViewById<EditText>(R.id.editTextSetPrice)

        val alertDialog = AlertDialog.Builder(this@ChatActivity)
            .setView(dialogView)
            .create()



        val updateButton = dialogView.findViewById<Button>(R.id.buttonSetPriceConfirm)
        updateButton.setOnClickListener {
            val serviceId = intent.getStringExtra("serviceId")?.toIntOrNull()
            val negotiationId = intent.getStringExtra("negotiationId")?.toIntOrNull()
            val freelancerPrice = tvSetPrice.text.toString().toDoubleOrNull()

            var negotiation: Negotiation? = null

            if (serviceId != null) {
                negotiation = Negotiation(
                    serviceId = serviceId,
                    freelancerPrice = freelancerPrice,
                    // Set other fields as necessary
                )
            } else {
                negotiation = Negotiation(
                    customerPrice = freelancerPrice,
                    // Set other fields as necessary
                )
            }


                val call = NegotiationInstance.retrofitBuilder
                call.updateNegotiation(negotiationId.toString(), negotiation).enqueue(object:
                    Callback<Negotiation> {
                    override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                        if (response.isSuccessful) {
                            // Handle successful update
                            Toast.makeText(this@ChatActivity, "Update successful", Toast.LENGTH_SHORT).show()
                        } else {
                            // Handle error response
                            Toast.makeText(this@ChatActivity, "Update failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                        // Handle failure
                        Toast.makeText(this@ChatActivity, "Update failed: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

        // Set the onClickListener for the close button
        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    }


}