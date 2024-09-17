package com.example.gotrabahomobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import com.example.gotrabahomobile.Model.UserFirebase
import com.example.gotrabahomobile.databinding.FragmentCustomerHomeBinding
import com.example.gotrabahomobile.databinding.FragmentSubservicesBinding
import com.example.gotrabahomobile.fragments.CustomerHomeFragment
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SubservicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubservicesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var selectedService: String? = null
    var userList = ArrayList<UserFirebase>()
    private var _binding: FragmentSubservicesBinding? = null
    private val binding get() = _binding!!
    private var email: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        email = arguments?.getString("email")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubservicesBinding.inflate(inflater, container, false)

        val longitude = arguments?.getDouble("longitude", 0.0) ?: 0
        val latitude = arguments?.getDouble("latitude", 0.0) ?: 0
        val userId = arguments?.getInt("userId", 0) ?: 0
        val buttonMapView: Button = _binding!!.buttonMapView
        buttonMapView.setOnClickListener{
            val intent = Intent(requireContext(), FreelancerListMapViewActivity::class.java)
            intent.putExtra("longitude", longitude)
            intent.putExtra("latitude", latitude)
            intent.putExtra("sqlId", userId)
            startActivity(intent)
        }

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val plumbing = view.findViewById<CardView>(R.id.plumbingSubservice)
        val electrical = view.findViewById<CardView>(R.id.electricalSubservice)
        val acRepair = view.findViewById<CardView>(R.id.acRepairSubservice)
        val refRepair = view.findViewById<CardView>(R.id.refRepairSubservice)
        val carpentry = view.findViewById<CardView>(R.id.carpentrySubservice)


        plumbing.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        electrical.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        acRepair.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        refRepair.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }

        carpentry.setOnClickListener {
            val userId = arguments?.getInt("userId", 0) ?: 0

            val bundle = Bundle().apply {
                putInt("userId", userId)
            }

            val homeFragment = CustomerHomeFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, homeFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SubservicesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubservicesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}