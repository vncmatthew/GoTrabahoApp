package com.example.gotrabahomobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrabahomobile.DTO.ServiceTypeListDTO
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.Wrapper.ServiceTypeList
import com.example.gotrabahomobile.databinding.ActivityCustomerHomePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerHomePageActivity : AppCompatActivity() {

    private lateinit var customerToolbar: androidx.appcompat.widget.Toolbar
    private lateinit var binding: ActivityCustomerHomePageBinding
    var selectedService: String? = null
    var userList = ArrayList<UserFirebase>()
    private lateinit var rvAdapter: ServiceAdapter
    private lateinit var serviceList: List<Services>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        serviceList = listOf()

        val serviceTypeName = findViewById<Spinner>(R.id.spinnerServiceName)
        val serviceTypesArray = resources.getStringArray(R.array.serviceTypes)
        val serviceTypesList = serviceTypesArray.toMutableList()

        val adapter = ArrayAdapter(this@CustomerHomePageActivity,
            android.R.layout.simple_spinner_item, serviceTypesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        serviceTypeName.adapter = adapter
        serviceTypeName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Store the selected item in the variable
                selectedService = parent.getItemAtPosition(position) as? String
                getServiceList(selectedService)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(this@CustomerHomePageActivity, "Please Select a Service", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getServiceList(select: String?){
        val service = ServicesInstance.retrofitBuilder
        val identification = intent.getIntExtra("userId", 0)

        service.getServicesType(select).enqueue(object : Callback<List<Services>> {
            override fun onResponse(
                call: Call<List<Services>>,
                response: Response<List<Services>>
            ) {
                if (response.isSuccessful && response.body() != null){
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
                                Log.d("TAG", "Value is: $user")
                                if (!user!!.userId.equals(firebase.uid)) {

                                    userList.add(user)
                                }
                            }
                        serviceList = response.body()!!

                        binding.rvMain.apply {
                            rvAdapter =
                                ServiceAdapter(serviceList, this@CustomerHomePageActivity, userList, identification)
                            adapter = rvAdapter
                            layoutManager = LinearLayoutManager(this@CustomerHomePageActivity)
                        }
                        }
                    })
                }
                }
            override fun onFailure(call: Call<List<Services>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }


}