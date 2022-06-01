package com.wadachirebandi.kiddietrackingdriver.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wadachirebandi.kiddietrackingdriver.daos.DriverDao
import com.wadachirebandi.kiddietrackingdriver.databinding.FragmentDriverBinding
import com.wadachirebandi.kiddietrackingdriver.models.Driver

class DriverFragment : Fragment() {

    private var _binding: FragmentDriverBinding? = null

    private val binding get() = _binding!!

    private val driverDao = DriverDao()

    private val auth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverBinding.inflate(layoutInflater, container, false)

        driverDao.driverCollection.document(auth.uid!!)
            .get().addOnSuccessListener {
                updateUi((it.toObject(Driver::class.java)))
            }

        return binding.root
    }

    private fun updateUi(driverDetails: Driver?) {
        if (driverDetails != null) {
            binding.driverName.text = driverDetails.driver_name
            binding.driverContentNumber.text = driverDetails.driver_number
        }

    }

}