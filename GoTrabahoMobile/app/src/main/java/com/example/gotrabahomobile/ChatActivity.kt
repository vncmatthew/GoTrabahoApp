package com.example.gotrabahomobile

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.ChatAdapter
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Chat
import com.example.gotrabahomobile.Model.Negotiation
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.NegotiationRemote.NegotiationInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.ArrayList

@Suppress("DEPRECATION")
class ChatActivity : AppCompatActivity() {

    private lateinit var etMessage: EditText
    private lateinit var btnSendMessage: ImageButton
    private lateinit var btnSetPrice: Button
    private lateinit var btnConfirmSetPrice: Button
//    private lateinit var imgBack: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvSetPrice:TextView
    private lateinit var chatRecyclerView: RecyclerView
    private val email: String? = null
    private var userData: UserFirebase? = null
    var topic = ""
    var chatList = ArrayList<Chat>()

    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        chatRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        etMessage = findViewById(R.id.etMessage)
        btnSendMessage = findViewById(R.id.btnSendMessage)
        tvUserName = findViewById(R.id.tvUserName)
        btnSetPrice = findViewById(R.id.buttonChatSetPrice)



        //back button
        val backButton: ImageButton = findViewById(R.id.back_buttonNavbar)
        backButton.setOnClickListener{
            finish()
        }

        //get UserModel

            var Id = intent.getIntExtra("serviceId", 0)
            var userId = intent.getStringExtra("userId")
            var firstName = intent.getStringExtra("firstName")
            var lastName = intent.getStringExtra("lastName")
            var email = intent.getStringExtra("email")

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

    @RequiresApi(Build.VERSION_CODES.O)
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

            val sqlId = intent.getStringExtra("sqlId")
            val serviceId: Int? = intent.getIntExtra("serviceId", 0)
            val name = intent.getStringExtra("serviceName")
            val negotiationId = intent.getIntExtra("negotiationId", 0)
            val freelancerPrice = tvSetPrice.text.toString().toDoubleOrNull()

            Log.d("ChatActivity", email.toString())

            if (serviceId != 0) {
                val track = NegotiationInstance.retrofitBuilder
                track.getNegotiationTracker("$sqlId$serviceId$name").enqueue(object : Callback<Negotiation> {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                        if (response.isSuccessful) {
                            Log.d("ChatActivity", email.toString())


                            val service = response.body()
                            service?.let {
                                val negotiation = Negotiation(
                                    negotiationId = service.negotiationId,
                                    freelancerPrice = freelancerPrice
                                )
                                patchNegotiation(service.negotiationId!!, negotiation)
                                checker(service.negotiationId, service.serviceId!!, service.customerId!! )
                                btnConfirmSetPrice = dialogView.findViewById(R.id.buttonSetPriceConfirm)
                                btnConfirmSetPrice.setOnClickListener {
                                    val intent = Intent(this@ChatActivity, PaymentActivity::class.java)
                                    intent.putExtra("email", email)
                                    intent.putExtra("negotiationId", service.negotiationId)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                    override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                        Log.e("Checkit", "Failed to get negotiation tracker", t)
                        Toast.makeText(this@ChatActivity, "Failed to get negotiation tracker", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {

                val negotiation = Negotiation(
                    negotiationId = negotiationId,
                    customerPrice = freelancerPrice
                )
                patchNegotiation(negotiationId, negotiation)


            }


        }

        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }


        alertDialog.show()
    }


    private fun patchNegotiation(negotiationId: Int, negotiation: Negotiation) {
        val call = NegotiationInstance.retrofitBuilder
        call.patchNegotiation(negotiationId, negotiation).enqueue(object : Callback<Negotiation> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Negotiation>, response: Response<Negotiation>) {
                if (response.isSuccessful) {
                    Log.d("Warframe", "${response.body()}")
                    val serviceIdcustomer: Int? = intent.getIntExtra("serviceIdcustomer", 0)
                    val sqlId: Int? = intent.getIntExtra("sqlId",0)
                    Toast.makeText(this@ChatActivity, "Update successful", Toast.LENGTH_SHORT).show()
                    checker(negotiationId, serviceIdcustomer!!, sqlId!! )

                } else {
                    Log.d("Checkit", "${response.body()}")
                    Toast.makeText(this@ChatActivity, "Update failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Negotiation>, t: Throwable) {
                Log.e("Checkit", "Update failed", t)
                Toast.makeText(this@ChatActivity, "Update failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checker(negotiationId: Int, serviceId: Int, userId: Int){


        val call = NegotiationInstance.retrofitBuilder

        call.getNegotiationPrice(negotiationId).enqueue(object: Callback<Double>{
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                if (response.isSuccessful) {
                    val finalPrice = response.body()



                    val call = NegotiationInstance.retrofitBuilder
                    call.getPriceChecker(negotiationId).enqueue(object: Callback<Boolean>{
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            var check = response.body()
                            if (response.isSuccessful){
                                if(check == true){

                                    var newBooking = Booking(
                                        customerId = userId,
                                        bookingDatetime = LocalDate.now().toString(),
                                        amount = finalPrice,
                                        serviceFee = finalPrice!! * 0.15,
                                        bookingStatus = 1,
                                        serviceId = serviceId,
                                        negotiationId = negotiationId )

                                    val book = BookingInstance.retrofitBuilder
                                    book.insertBooking(newBooking).enqueue(object: Callback<ResponseBody>{
                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                                        ) {
                                            val booking = response.body()
                                            if(response.isSuccessful){
                                                Toast.makeText(applicationContext, "Successfully Booked", Toast.LENGTH_SHORT).show()
                                                Log.d("Check", "${booking}")
                                                val serviceId: Int? = intent.getIntExtra("serviceId", 0)

                                                if (serviceId != 0){
                                                Intent(this@ChatActivity, FreelancerMainActivity::class.java)
                                                startActivity(intent)
                                                }else {
                                                    Intent(this@ChatActivity, CustomerMainActivity::class.java)
                                                    startActivity(intent)
                                                }

                                            }
                                            Log.d("Check", "${booking}")
                                        }

                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                            Log.d("Check", "${t}")
                                        }

                                    })
                                }
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })





    }


}