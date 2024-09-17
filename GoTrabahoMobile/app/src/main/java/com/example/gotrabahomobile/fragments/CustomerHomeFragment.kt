package com.example.gotrabahomobile.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gotrabahomobile.DTO.ServicesWUserId
import com.example.gotrabahomobile.DTO.SubServicesTypes
import com.example.gotrabahomobile.FreelancerListMapViewActivity
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Helper.SubServiceAdapter
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.R
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.SubservicesFragment
import com.example.gotrabahomobile.databinding.FragmentCustomerHomeBinding
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
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CustomerHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CustomerHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var subServiceSpinner: Spinner
    var userList = ArrayList<UserFirebase>()
    private lateinit var rvAdapter: ServiceAdapter
    private lateinit var serviceList: List<ServicesWUserId>
    private var _binding: FragmentCustomerHomeBinding? = null



    private val binding get() = _binding!!

    private var email: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        subServiceSpinner = binding.spinnerServiceNameHome
        fetchSubService()
        email = arguments?.getString("email")
        Log.d("YourFragment", "Arguments: $arguments")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerHomeBinding.inflate(inflater, container, false)
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subServiceSpinner = binding.spinnerServiceNameHome

        fetchSubService()

        val backButton = view.findViewById<ImageButton>(R.id.back_buttonNavbar)
        backButton.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
            }

            val homeFragment = SubservicesFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun fetchSubService() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val call = ServicesInstance.retrofitBuilder
            val serviceType = arguments?.getString("serviceTypeName")
            call.getSubServicesPerService(serviceType).enqueue(object : Callback<List<SubServicesTypes>> {
                override fun onResponse(call: Call<List<SubServicesTypes>>, response: Response<List<SubServicesTypes>>) {
                    if (response.isSuccessful) {
                        val subserviceResponse = response.body()
                        if (subserviceResponse != null) {
                            val adapter = SubServiceAdapter(requireContext(), subserviceResponse)
                            subServiceSpinner.adapter = adapter

                            // Now it's safe to get the selected item
                            val selectedSubService = subServiceSpinner.selectedItem as? SubServicesTypes
                            selectedSubService?.let {
                                val subServiceId = it.subServiceName
                                getServiceList(subServiceId!!)
                            }
                        }
                    } else {
                        Log.e(ContentValues.TAG, "Error fetching SubServices")
                    }
                }

                override fun onFailure(call: Call<List<SubServicesTypes>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }


    private fun getServiceList(select: String){
        val service = ServicesInstance.retrofitBuilder
        val identification = arguments?.getInt("userId", 0) ?: 0

        service.getServicesPerSubService(select).enqueue(object : Callback<List<ServicesWUserId>> {
            override fun onResponse(
                call: Call<List<ServicesWUserId>>,
                response: Response<List<ServicesWUserId>>
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
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
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
                                    ServiceAdapter(serviceList, requireContext(), userList, identification)
                                adapter = rvAdapter
                                layoutManager = LinearLayoutManager(requireContext())

                            }
                        }
                    })


                }
            }
            override fun onFailure(call: Call<List<ServicesWUserId>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CustomerHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CustomerHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}