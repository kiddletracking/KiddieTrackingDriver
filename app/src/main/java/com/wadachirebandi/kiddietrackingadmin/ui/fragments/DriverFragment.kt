package com.wadachirebandi.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.wadachirebandi.kiddietrackingadmin.daos.DriverDao
import com.wadachirebandi.kiddietrackingadmin.databinding.FragmentDriverBinding
import com.wadachirebandi.kiddietrackingadmin.models.Driver

class DriverFragment : Fragment() {

    private var _binding: FragmentDriverBinding? = null

    private val binding get() = _binding!!

    private val driverDao = DriverDao()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDriverBinding.inflate(layoutInflater, container, false)

        driverDao.driverCollection.document("OtQfKg2Qxi6QAvzQIMLG")
            .get().addOnSuccessListener {
                updateUi((it.toObject(Driver::class.java)))
            }

        binding.saveDriverDetailsButton.setOnClickListener {
            if (binding.driverName.text.toString() != ""
                && binding.driverContentNumber.text.toString() != ""
            ) {
                updateDriverDetails()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Edit all details before saving",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    private fun updateDriverDetails() {
        val driver =
            Driver(binding.driverName.text.toString(), binding.driverContentNumber.text.toString())
        driverDao.driverCollection.document("OtQfKg2Qxi6QAvzQIMLG").set(driver)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Driver Details Edited Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }
    }

    private fun updateUi(driverDetails: Driver?) {
        if (driverDetails != null) {
            binding.driverName.setText(driverDetails.driver_name)
            binding.driverContentNumber.setText(driverDetails.driver_number)
        }

    }

}