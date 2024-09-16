package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gotrabahomobile.DTO.BookingUserDTO
import com.example.gotrabahomobile.DTO.FreelancerDashboard
import com.example.gotrabahomobile.Helper.ActivityRecycleViewAdapter
import com.example.gotrabahomobile.Model.Booking
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import com.example.gotrabahomobile.databinding.FragmentCustomerActivityBinding
import com.example.gotrabahomobile.databinding.FragmentFreelancerDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text
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
 * Use the [FreelancerDashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreelancerDashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentFreelancerDashboardBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentFreelancerDashboardBinding.inflate(inflater, container, false)
        arguments?.let {
            var Id = it.getInt("serviceId", 0)
            var userId = it.getInt("userId", 0)
            var freelancerId = it.getInt("freelancerId", 0)
            var firstName = it.getString("firstName").toString()
            var lastName = it.getString("lastName").toString()
            var email = it.getString("email").toString()

        }
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize RecyclerView
        var totalEarnings = view.findViewById<TextView>(R.id.TotalEarnings)
        var pendingBookings = view.findViewById<TextView>(R.id.PendingBookings)
        var totalBookings = view.findViewById<TextView>(R.id.TotalBookings)

        val viewPending = view.findViewById<TextView>(R.id.ViewPendingBookings)
        val viewTotalBookings = view.findViewById<TextView>(R.id.ViewTotalBookings)

        getDashboard(totalEarnings, pendingBookings,totalBookings)

        viewTotalBookings.setOnClickListener {

            val userId = arguments?.getInt("userId", 0) ?: 0
            val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
            val firstName = arguments?.getString("firstName")
            val lastName = arguments?.getString("lastName")
            val email = arguments?.getString("email")
            val fullName = arguments?.getString("fullName")

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putInt("freelancerId", freelancerId)
                putString("firstName", firstName)
                putString("lastName", lastName)
                putString("email", email)
                putString("fullName", fullName)
                putInt("initialTab", 3)
            }

            val bookingsFragment = BookingsFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.freelancerMainContainer, bookingsFragment)
                .addToBackStack(null)
                .commit()

            updateNavigationBarState(R.id.bookingsFragment)
        }

        viewPending.setOnClickListener {

            val userId = arguments?.getInt("userId", 0) ?: 0
            val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
            val firstName = arguments?.getString("firstName")
            val lastName = arguments?.getString("lastName")
            val email = arguments?.getString("email")
            val fullName = arguments?.getString("fullName")

            val bundle = Bundle().apply {
                putInt("userId", userId)
                putInt("freelancerId", freelancerId)
                putString("firstName", firstName)
                putString("lastName", lastName)
                putString("email", email)
                putString("fullName", fullName)
            }

            val bookingsFragment = BookingsFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.freelancerMainContainer, bookingsFragment)
                .addToBackStack(null)
                .commit()

            updateNavigationBarState(R.id.bookingsFragment)

//            val intent = Intent(activity, FreelancerMainActivity::class.java).apply {
//                putExtras(bundle)
//            }
//
//            activity?.startActivity(intent)

        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FreelancerDashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FreelancerDashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getDashboard(totalEarnings: TextView, pendingBookings: TextView, totalBooking: TextView){
        val freelancer = FreelancerInstance.retrofitBuilder
        val freelancerId = arguments?.getInt("freelancerId")
        freelancer.getDashboard(freelancerId!!).enqueue(object: Callback<FreelancerDashboard>{
            override fun onResponse(
                call: Call<FreelancerDashboard>,
                response: Response<FreelancerDashboard>
            ) {
                if(response.isSuccessful){
                    totalEarnings.text = "₱ " + response.body()!!.totalEarnings?.toString()
                    pendingBookings.text = response.body()!!.pendingJobs?.toString()
                    totalBooking.text = response.body()!!.totalBookings?.toString()
                }
            }

            override fun onFailure(call: Call<FreelancerDashboard>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    fun updateNavigationBarState(tabId: Int) {
        activity?.let { activity ->
            val bottomNavigationView =
                activity.findViewById<BottomNavigationView>(R.id.freelancerBottomNavigationView)
            when (tabId) {
                R.id.bookingsFragment -> {
                    bottomNavigationView.menu.findItem(R.id.bookingsFragment)?.isChecked = true
                }
            }
        }
    }

}