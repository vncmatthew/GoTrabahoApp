//package com.example.gotrabahomobile
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.gotrabahomobile.Helper.ServiceAdapter
//import com.example.gotrabahomobile.Model.Services
//import com.example.gotrabahomobile.Model.UserFirebase
//import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
//import com.example.gotrabahomobile.databinding.FragmentCustomerHomeBinding
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.messaging.FirebaseMessaging
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.util.ArrayList
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [CustomerHomeFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class CustomerHomeFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//    var selectedService: String? = null
//    var userList = ArrayList<UserFirebase>()
//    private lateinit var rvAdapter: ServiceAdapter
//    private lateinit var serviceList: List<Services>
//    private var _binding: FragmentCustomerHomeBinding? = null
//    private val binding get() = _binding!!
//
//    private var email: String? = null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//
//        arguments?.let {
//            param1 = it.getString(com.example.gotrabahomobile.ARG_PARAM1)
//            param2 = it.getString(com.example.gotrabahomobile.ARG_PARAM2)
//        }
//
//        email = arguments?.getString("email")
//        Log.d("YourFragment", "Arguments: $arguments")
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
//
//        val buttonPay: Button = _binding!!.buttonPayment
//        buttonPay.setOnClickListener{
//            val intent = Intent(requireContext(), PaymentActivity::class.java)
//            intent.putExtra("email", email)
//            startActivity(intent)
//        }
//
//        val spinner: Spinner = _binding!!.spinnerServiceNameHome
//        Log.d("CustomerHomeFragment", "Spinner found: $spinner")
//
//        val adapter = ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.serviceTypes,
//            android.R.layout.simple_spinner_item
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
//        Log.d("CustomerHomeFragment", "Adapter set: ${spinner.adapter}")
//
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                selectedService = parent.getItemAtPosition(position) as? String
//                getServiceList(selectedService)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                Toast.makeText(requireContext(), "Please Select a Service", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//
//        return _binding!!.root
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }
//
//    private fun getServiceList(select: String?){
//        val service = ServicesInstance.retrofitBuilder
//        val identification = arguments?.getInt("userId", 0) ?: 0
//
//        service.getServicesType(select).enqueue(object : Callback<List<Services>> {
//            override fun onResponse(
//                call: Call<List<Services>>,
//                response: Response<List<Services>>
//            ) {
//                if (response.isSuccessful && response.body() != null){
//                    val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//                    var userid = firebase.uid
//                    FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")
//
//
//                    val databaseReference: DatabaseReference =
//                        FirebaseDatabase.getInstance().getReference("UserFirebase")
//                    databaseReference.addValueEventListener(object : ValueEventListener {
//                        override fun onDataChange(dataSnapshot: DataSnapshot) {
//                            Log.d("DEBUG", "Path: ${dataSnapshot.ref.path}")
//                            Log.d("DEBUG", "Data: ${dataSnapshot.value}")
//                            val user = dataSnapshot.getValue(UserFirebase::class.java)
//                            Log.d("DEBUG", "User: $user")
//                        }
//
//                        override fun onCancelled(databaseError: DatabaseError) {
//                            Log.w("DEBUG", "Failed to read value.", databaseError.toException())
//                        }
//                    })
//
//                    databaseReference.addValueEventListener(object : ValueEventListener {
//                        override fun onCancelled(error: DatabaseError) {
//                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            userList.clear()
//
//                            for (dataSnapShot: DataSnapshot in snapshot.children) {
//                                val user = dataSnapShot.getValue(UserFirebase::class.java)
//                                Log.d("TAG", "Value is: $user")
//                                if (!user!!.userId.equals(firebase.uid)) {
//
//                                    userList.add(user)
//                                }
//                            }
//                            serviceList = response.body()!!
//
//                            binding.rvMain.apply {
//                                rvAdapter =
//                                    ServiceAdapter(
//                                        serviceList,
//                                        requireContext(),
//                                        userList,
//                                        identification
//                                    )
//                                adapter = rvAdapter
//                                layoutManager = LinearLayoutManager(requireContext())
//                            }
//                        }
//                    })
//                }
//            }
//            override fun onFailure(call: Call<List<Services>>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//
//        })
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment CustomerHomeFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            CustomerHomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(com.example.gotrabahomobile.ARG_PARAM1, param1)
//                    putString(com.example.gotrabahomobile.ARG_PARAM2, param2)
//                }
//            }
//    }
//}