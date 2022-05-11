package com.wadachirebandi.kiddietrackingadmin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wadachirebandi.kiddietrackingadmin.databinding.FragmentJourneyBinding
import com.wadachirebandi.kiddietrackingadmin.notification.NotificationData
import com.wadachirebandi.kiddietrackingadmin.notification.PushNotification
import com.wadachirebandi.kiddietrackingadmin.notification.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TOPIC = "/topics/journey"

class JourneyFragment : Fragment() {

    private var _binding: FragmentJourneyBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJourneyBinding.inflate(layoutInflater, container, false)

        binding.startJourneyButton.setOnClickListener {
            startJourney()
        }

        return binding.root
    }

    private fun startJourney() {
        PushNotification(
            NotificationData("Child Absent", "Your Child is absent today"),
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