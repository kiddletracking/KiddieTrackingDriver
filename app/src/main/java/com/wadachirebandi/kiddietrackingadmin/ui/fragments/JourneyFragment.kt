package com.wadachirebandi.kiddietrackingadmin.ui.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.wadachirebandi.kiddietrackingadmin.R
import com.wadachirebandi.kiddietrackingadmin.daos.LocationDao
import com.wadachirebandi.kiddietrackingadmin.databinding.FragmentJourneyBinding
import com.wadachirebandi.kiddietrackingadmin.models.Location
import com.wadachirebandi.kiddietrackingadmin.notification.NotificationData
import com.wadachirebandi.kiddietrackingadmin.notification.PushNotification
import com.wadachirebandi.kiddietrackingadmin.notification.RetrofitInstance
import com.wadachirebandi.kiddietrackingadmin.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TOPIC = "/topics/journey"

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null

    private val binding get() = _binding!!

    private var liveLocation = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyBinding.inflate(layoutInflater, container, false)

        checkJourney()

        binding.startJourneyButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.delete_dialog)
            dialog.show()
            val yes = dialog.findViewById<AppCompatButton>(R.id.btn_yes_dialog)
            val no = dialog.findViewById<AppCompatButton>(R.id.btn_no_dialog)
            val message = dialog.findViewById<TextView>(R.id.message)
            message.text =
                "Are you sure, you want to start a journey. You should not close the application" +
                        " after starting a journey as parents need to get the live location"
            yes.setOnClickListener {
                startJourney()
                dialog.cancel()
            }
            no.setOnClickListener {
                Toast.makeText(requireContext(), "You cancelled a journey", Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }
        }

        binding.endJourneyButton.setOnClickListener {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.delete_dialog)
            dialog.show()
            val yes = dialog.findViewById<AppCompatButton>(R.id.btn_yes_dialog)
            val no = dialog.findViewById<AppCompatButton>(R.id.btn_no_dialog)
            val message = dialog.findViewById<TextView>(R.id.message)
            message.text =
                "Are you sure, you want to end this journey"
            yes.setOnClickListener {
                endJourney()
            }
            no.setOnClickListener {
                Toast.makeText(requireContext(), "You cancelled a journey", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun endJourney() {
        LocationDao().locationCollection.set(Location(false))
        startActivity(Intent(requireContext(), MainActivity::class.java))
        (activity as MainActivity).finish()
    }

    private fun checkJourney() {
        LocationDao().locationCollection.get().addOnSuccessListener {
            liveLocation = it["live_location"] as Boolean
            if (liveLocation) {
                binding.startJourneyButton.visibility = View.GONE
            }
        }
    }

    private fun startJourney() {
        binding.startJourneyButton.visibility = View.GONE
        LocationDao().locationCollection.set(Location(true))
        CoroutineScope(Dispatchers.IO).launch {
            (activity as MainActivity).getLiveLocation()
        }
        PushNotification(
            NotificationData("Bus Location", "Bus have started a journey"),
            TOPIC
        ).also {
            sendNotification(it)
        }
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitInstance.api.postNotification(notification)
        }

}