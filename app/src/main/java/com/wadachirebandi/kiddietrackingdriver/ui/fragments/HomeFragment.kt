package com.wadachirebandi.kiddietrackingdriver.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.wadachirebandi.kiddietrackingdriver.R
import com.wadachirebandi.kiddietrackingdriver.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.childrenDetails.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userDetailsFragment)
        }

        binding.driverDetails.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_driverFragment)
        }

        binding.journey.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_journeyFragment)
        }

        return binding.root
    }
}