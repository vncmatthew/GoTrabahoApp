package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.example.gotrabahomobile.Model.Freelancer
import com.example.gotrabahomobile.Remote.FreelancerRemote.FreelancerInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FreelancerAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FreelancerAccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var freelancerActivateStatus: Switch
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_freelancer_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val freelancerActivateStatus = view.findViewById<Switch>(R.id.ActiveStatusSwitch)
        val freelancerCardProfile = view.findViewById<androidx.cardview.widget.CardView>(R.id.freelancerProfile)
        val freelancerCardAddNewService = view.findViewById<androidx.cardview.widget.CardView>(R.id.addNewService)
        val freelancerCardEditServices = view.findViewById<androidx.cardview.widget.CardView>(R.id.editServices)
        val freelancerCardReportBug = view.findViewById<androidx.cardview.widget.CardView>(R.id.freelancerReportBug)

        val freelancerCardLogout = view.findViewById<androidx.cardview.widget.CardView>(R.id.freelancerLogout)

        val userId = arguments?.getInt("userId", 0) ?: 0
        val freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
        val firstName = arguments?.getString("firstName")
        val lastName = arguments?.getString("lastName")
        val email = arguments?.getString("email")
        val fullName = arguments?.getString("fullName")

        Log.d("FreelancerAccountFragment", freelancerId.toString())
        Log.d("FreelancerAccountFragment", "${email}")
        freelancerActivateStatus?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                changeStatusTrue()
                Toast.makeText(requireActivity(), "Showing your Services", Toast.LENGTH_SHORT).show()
            } else {
                changeStatusFalse()
                Toast.makeText(requireActivity(), "email or password invalid", Toast.LENGTH_SHORT).show()
            }

        }
        freelancerCardProfile.setOnClickListener{
            val intent = Intent(requireActivity(), FreelancerProfilePageActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        freelancerCardAddNewService.setOnClickListener{
            val intent = Intent(requireActivity(), FreelancerNewServiceJobVerificationActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        freelancerCardLogout.setOnClickListener{
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        freelancerCardEditServices.setOnClickListener{
            val intent = Intent(requireActivity(), FreelancerServicesListActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("freelancerId", freelancerId)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("fullName", fullName)
            intent.putExtra("email", email)
            startActivity(intent)
        }

        freelancerCardReportBug.setOnClickListener {
            val intent = Intent(requireActivity(), BugReportActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

    }

    fun changeStatusTrue(){


        var freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
        val call = FreelancerInstance.retrofitBuilder
        val freelancer = Freelancer(
            freelancerId = freelancerId,
            verificationStatus = true
            )
        call.patchFreelancer(freelancerId, freelancer).enqueue(object: Callback<Freelancer>{
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun changeStatusFalse(){


        var freelancerId = arguments?.getInt("freelancerId", 0) ?: 0
        val call = FreelancerInstance.retrofitBuilder
        val freelancer = Freelancer(
            freelancerId = freelancerId,
            verificationStatus = false
        )
        call.patchFreelancer(freelancerId, freelancer).enqueue(object: Callback<Freelancer>{
            override fun onResponse(call: Call<Freelancer>, response: Response<Freelancer>) {
                if(response.isSuccessful){
                    Log.d("Changed Bookings", "True")
                }
            }

            override fun onFailure(call: Call<Freelancer>, t: Throwable) {
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
         * @return A new instance of fragment FreelancerAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FreelancerAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}