package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.Helper.BookingFreelancerAdapter
import com.example.gotrabahomobile.Helper.ServiceAdapter
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Model.Services
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.Remote.BookingRemote.BookingInstance
import com.example.gotrabahomobile.Remote.ServicesRemote.ServicesInstance
import com.example.gotrabahomobile.databinding.ActivityFreelancerBookingsPageBinding
import com.example.gotrabahomobile.databinding.FragmentBookingsBinding
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentBookingsBinding? = null
    var userList = ArrayList<UserFirebase>()
    private lateinit var rvsAdapter: ServiceAdapter
    private lateinit var serviceList: List<Services>
    private lateinit var rvAdapter: BookingFreelancerAdapter
    private lateinit var bookingList: List<Booking>
    var selectedService: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)

        val spinner : Spinner = _binding!!.spinnerBookingsServiceName

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.serviceTypes,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        Log.d("BookingsFragment", "Adapter set: ${spinner.adapter}")



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedService = parent.getItemAtPosition(position) as? String

//                getBookingList(selectedService)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(requireContext(), "Please Select a Service", Toast.LENGTH_SHORT).show()
            }
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getInt("userId", 0) ?: 0
        val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val email = arguments?.getString("email")
        val fullName = arguments?.getString("fullName")



        Log.d("BookingsFragment", email.toString())

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("Pending"))
        tabLayout.addTab(tabLayout.newTab().setText("Ongoing"))
        tabLayout.addTab(tabLayout.newTab().setText("Completed"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> {
                        selectedService?.let { getBookingList(1, it) }
                    }
                    1 -> {
                        selectedService?.let { getBookingList(2, it) }
                    }
                    2 -> {
                        selectedService?.let { getBookingList(3, it) }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        })



        Log.d("BookingsFragment", freelancerId.toString())
        Log.d("BookingsFragment", "${email}")
    }

    private fun getBookingList(status: Int, selectedService: String){
        val service = BookingInstance.retrofitBuilder

        val freelancerId = arguments?.getInt("freelancerId", 0)
        val nullableFreelancerId = freelancerId ?: 0
        Log.d("BookingsFrag", nullableFreelancerId.toString())
        service.getBookingStatus(nullableFreelancerId,status, selectedService).enqueue(object :
            Callback<List<Booking>> {
            override fun onResponse(
                call: Call<List<Booking>>,
                response: Response<List<Booking>>
            ) {
                if (response.isSuccessful && response.body() != null){
                    bookingList = response.body()!!
                    _binding!!.rvFreelancerHome.apply {
                        val email = arguments?.getString("email")
                        rvAdapter = BookingFreelancerAdapter(bookingList, requireContext(), email, status)
                        adapter = rvAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                } else{
                    Log.d("TestActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Booking>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun getServiceId() {
        val call = ServicesInstance.retrofitBuilder
        val freelancerId = arguments?.getInt("freelancerId", 0)
        val nullableFreelancerId = freelancerId ?: 0

        Log.d("BookingsFrag", nullableFreelancerId.toString())
        selectedService?.let {
            call.getServiceIdByFreelancer(nullableFreelancerId, it).enqueue(object:
                Callback<List<Services>> {
                override fun onResponse(call: Call<List<Services>>, response: Response<List<Services>>) {
                    if (response.isSuccessful) {
                        val services = response.body()

                        val serviceId = services?.firstOrNull()?.serviceId
                        if (serviceId != null) {
                            val intent = Intent(requireContext(), FreelancerMessagesActivity::class.java)
                            intent.putExtra("serviceId", serviceId)
                            intent.putExtra("serviceName", services?.firstOrNull()?.name)
                            startActivity(intent)
                        } else {
                            Toast.makeText(requireContext(), "No Service Id", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "No Service Id", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Services>>, t: Throwable) {
                    Log.d("Check", "{$t}")
                }
            })
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}